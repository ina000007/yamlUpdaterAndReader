package MyYaml.YamlUpdater.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlReader {
	Properties prop = new Properties();
	private Map<String, Object> yamlPage;
	InputStream in;
	Yaml yaml;
	String file;
	public YamlReader(String fileName) throws IOException {
		file = fileName;
		in = new FileInputStream(fileName);
		yaml = new Yaml();
		yamlPage = (Map<String, Object>) yaml.load(in);
	}

	public Object elementPath(String elementPath) {
		return elementValue(elementPath, yamlPage);
	}
	public void set(String elementPath,String val) throws IOException {
		 setVal(elementPath, yamlPage,val);
	}

	private Object elementValue(String elementPath, Map<String, Object> yamlPage) {
		Object o=null;
		String[] path = elementPath.split("\\.");
		for(String p :path) {
			if(yamlPage.get(p) instanceof String)
				return yamlPage.get(p);
			yamlPage=(Map<String, Object>)yamlPage.get(p);
			o=yamlPage;
			}
		return o;
	}
	
	private void  setVal(String elementPath, Map<String, Object> yamlPage,String val) throws IOException {
		String[] path = elementPath.split("\\.");
		for(String p :path) {
			if(yamlPage.get(p) instanceof String) {
				 yamlPage.put(p,val);
			     break;	 
			}
			yamlPage=(Map<String, Object>)yamlPage.get(p);
			}
		yaml.dumpAsMap(yamlPage);
		in.close();
		
	}
    private void replace(Map<String, Object> yamlPage, String elementPath, String replacement) {
        String[] path = elementPath.split("\\.");
        if (path.length == 1) {
            yamlPage.put(path[0], replacement);
            return;
        }
        replace((Map<String, Object>) yamlPage.get(path[0]), elementPath.substring(elementPath.indexOf(".") + 1),
                replacement);
    }
    public void valReplace(String path,String val) throws IOException {
    	replace(yamlPage,path,val);
    	DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml rewrite = new Yaml(options);
        FileWriter writer = new FileWriter(file);
        rewrite.dump(yamlPage, writer);
    }
	public static void main(String[] args) throws IOException {
		YamlReader yamlReader = new YamlReader("data.yaml");
		System.out.println(yamlReader.elementPath("users.tom"));
		yamlReader.valReplace("users.tom","1111111");
		System.out.println(yamlReader.elementPath("users.tom"));

	}

}