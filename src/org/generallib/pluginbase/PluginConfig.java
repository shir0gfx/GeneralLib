package org.generallib.pluginbase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.generallib.serializetools.Utf8YamlConfiguration;

/**
 * <p>
 * Let automatically make public fields to be saved and loaded.
 * </p>
 * 
 * Child class only need to declare <b>public field with '_'</b> as _ will be
 * used to indicate the path. Fields with other than public modifier will be
 * ignored.
 * 
 * <p>
 * For example) test_test field is equivalent to test.test in config
 * </p>
 * 
 * @author wysohn
 *
 */
public abstract class PluginConfig implements PluginProcedure{
/*	public static void main(String[] ar){
		System.out.println(convertToConfigName("test_test_test"));
		System.out.println(converToFieldName("test.test.test"));
	}*/

	protected PluginBase base;
	
	private FileConfiguration config;
	private File file;
	
	public int Command_Help_SentencePerPage = 6;
	
	public String Plugin_Language_Default = "en";
	public List<String> Plugin_Language_List = new ArrayList<String>(){{
		add("en");
		add("ko");
	}};

	public void onEnable(PluginBase base) throws Exception {
		this.base = base;
		config = new Utf8YamlConfiguration();
		
		file = new File(base.getDataFolder(), "config.yml");
		if(!file.exists())
			file.createNewFile();
		
		config.load(file);
		
		validateAndLoad();
	}

	public void onDisable(PluginBase base) throws Exception{
		save();
	}

	public void onReload(PluginBase base) throws Exception {
		reload();
	}

	private static String convertToConfigName(String fieldName){
		return fieldName.replaceAll("_", ".");
	}
	
	private static String converToFieldName(String configKey){
		return configKey.replaceAll("\\.", "_");
	}
	
	/**
	 * check all the config and add necessary/remove unnecessary configs.
	 */
	protected void validateAndLoad(){
		base.getLogger().info("Validating config...");
		
		Field[] fields = this.getClass().getFields();
		
		int addedNew = 0;
		//fill empty config
		for(Field field : fields){
			try {
				String configName = convertToConfigName(field.getName());
				Object obj = field.get(this);
				
				if(!config.contains(configName)){
					config.set(configName, obj);
					addedNew++;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				base.getLogger().severe(e.getMessage());
			}
		}
		base.getLogger().info("Added ["+addedNew+"] new configs with default value.");
		
		Set<String> fieldNames = new HashSet<String>();
		for(Field field : fields)
			fieldNames.add(field.getName());
		
		int deletedOld = 0;
		int loaded = 0;
		//delete non existing config or set value with existing config
		Configuration root = config.getRoot();
		Set<String> keys = root.getKeys(true);
		for(String key : keys){
			try {
				if(config.isConfigurationSection(key))
					continue;
				
				String fieldName = converToFieldName(key);
				
				if(!fieldNames.contains(fieldName)){
					config.set(key, null);
					deletedOld++;
				}else{
					Field field = this.getClass().getField(fieldName);
					
					field.set(this, config.get(key));
					loaded++;
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				base.getLogger().severe(e.getMessage());
			}
		}
		base.getLogger().info("Deleted ["+deletedOld+"] old configs and loaded ["+loaded+"] configs.");
		
		base.getLogger().info("Validation and Loading complete!");
	}
	
	/**
	 * save current values into the config file
	 * @throws IOException
	 */
	public void save() throws IOException{
		base.getLogger().info("Saving to ["+file.getName()+"]...");
		config.save(file);
		base.getLogger().info("Complete!");
	}
	
	/**
	 * Override all current values using values in config file
	 * @throws IOException
	 * @throws InvalidConfigurationException
	 */
	public void reload() throws IOException, InvalidConfigurationException{
		base.getLogger().info("Loading ["+file.getName()+"]...");
		config.load(file);
		base.getLogger().info("Complete!");
		
		validateAndLoad();
	}
}
