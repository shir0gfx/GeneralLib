package org.generallib.pluginbase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.generallib.pluginbase.language.DefaultLanguages;
import org.generallib.serializetools.Utf8YamlConfiguration;
import org.generallib.serializetools.exceptions.FileSerializeException;

public final class PluginLanguage implements PluginProcedure {
	private final String defaultLang;
	private final Set<String> supportLanguages;
	private final Map<String, LanguageFileSession> langFiles = new HashMap<String, LanguageFileSession>();
	private final Set<Language> languages = new HashSet<Language>(){{
		for(Language lang : DefaultLanguages.values())
			add(lang);
	}};
	
	private PluginBase base;
	
	private Queue<Double> doub = new LinkedList<Double>();
	private Queue<Integer> integer = new LinkedList<Integer>();
	private Queue<String> string = new LinkedList<String>();
	private Queue<Boolean> bool = new LinkedList<Boolean>();
	
	public PluginLanguage(Set<String> supportLanguages, String defaultLang){
		Validate.notNull(supportLanguages);
		Validate.notNull(defaultLang);
		
		this.supportLanguages = supportLanguages;
		this.defaultLang = defaultLang;
	}
	
	public PluginLanguage(Set<String> supportLanguages){
		this(supportLanguages, Locale.ENGLISH.toString());
	}
	
	@Override
	public void onEnable(PluginBase base) throws Exception {
		this.base = base;
		
		File langFolder = new File(base.getDataFolder(), "lang");
		
		if(!langFolder.exists())
			langFolder.mkdirs();
		
		for(String lang : supportLanguages){
			File file = new File(langFolder, lang+".yml");
			LanguageFileSession session = null;
			
			try {
				session = new LanguageFileSession(file);
			} catch (IOException e) {
				base.getLogger().severe("While creating file [" + lang + ".yml]: ");
				base.getLogger().severe(e.getMessage());
			}
			
			langFiles.put(lang, session);
		}
		
		if(!langFiles.containsKey(defaultLang)){
			throw new Exception("default language file["+defaultLang+".yml] doesn't exist!");
		}
		
		for(Entry<String, LanguageFileSession> entry : langFiles.entrySet()){
			fillIfEmpty(entry);
		}
	}

	@Override
	public void onDisable(PluginBase base) throws Exception{
		save();
	}

	@Override
	public void onReload(PluginBase base) throws Exception {
		reload();
	}
	
	private void save() throws IOException{
		for(Entry<String, LanguageFileSession> entry : langFiles.entrySet()){
			entry.getValue().save();
		}
	}
	
	private void reload() throws FileNotFoundException, IOException, InvalidConfigurationException{
		for(Entry<String, LanguageFileSession> entry : langFiles.entrySet()){
			base.getLogger().info("reloading language ["+entry.getKey()+"]...");
			entry.getValue().reload();
			base.getLogger().info("loaded!");
		}
	}
	
	/**
	 * 
	 * @param lang
	 * @return true if there was no same Language already registered
	 */
	public boolean registerLanguage(Language lang){
		return languages.add(lang);
	}
	
	private void fillIfEmpty(Entry<String, LanguageFileSession> entry){
		String locale = entry.getKey();
		LanguageFileSession session = entry.getValue();
		
		int i = 0;
		for(final Language lang : languages){
			String str = lang.toString();
			
			if(session.config.get(str) == null){
				session.config.set(str, new ArrayList<String>(){{
					addAll(Arrays.asList(lang.getEngDefault()));
				}});
				i++;
			}
			
			try {
				session.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		base.getLogger().info("Added ["+i+"] new langauges to ["+locale+"] locale.");
	}
	
	public void addDouble(double doub){
		this.doub.add(Double.valueOf(doub));
	}
	
	public void addInteger(int integer){
		this.integer.add(Integer.valueOf(integer));
	}
	
	public void addString(String string){
		Validate.notNull(string);
		
		this.string.add(string);
	}
	
	public void addBoolean(boolean bool){
		this.bool.add(Boolean.valueOf(bool));
	}
	
	public String parseFirstString(Language lang){
		String[] result = parseStrings(null, lang, null);
		return result.length < 1 ? "NULL" : result[0];
	}
	
	public String parseFirstString(CommandSender sender, Language lang){
		String[] result = parseStrings(sender, lang, null);
		return result.length < 1 ? "NULL" : result[0];
	}
	
	public String parseFirstString(CommandSender sender, Locale locale, Language lang){
		String[] result = parseStrings(sender, lang, locale.toString());
		return result.length < 1 ? "NULL" : result[0];
	}
	
	public String parseFirstString(CommandSender sender, Language lang, String locale){
		String[] result = parseStrings(sender, lang, locale);
		return result.length < 1 ? "NULL" : result[0];
	}
	
	public String[] parseStrings(Language lang){
		return parseStrings(null, lang, null);
	}
	
	public String[] parseStrings(CommandSender sender, Language lang){
		return parseStrings(sender, lang, null);
	}
	
	public String[] parseStrings(CommandSender sender, Locale locale, Language lang){
		return parseStrings(sender, lang, locale.toString());
	}
	
	@SuppressWarnings("unchecked")
	public String[] parseStrings(CommandSender sender, Language lang, String locale){
		if(locale == null)
			locale = defaultLang;
		
		Validate.notNull(lang);
		
		LanguageFileSession session = langFiles.get(locale);
		if(session == null)
			session = langFiles.get(defaultLang);
		
		if(session == null){
			base.getLogger().severe("Cannot parse language with locale "+locale+"!");
			return new String[]{};
		}
		
		List<String> str = new ArrayList<String>();
		str.addAll((List<String>) session.config.get(lang.toString()));
		
		Validate.notNull(str);
		
		replaceVariables(sender, str);
		
		return str.toArray(new String[str.size()]);
	}
	
	/**
	 * @author Hex_27
	 * @param msg
	 * @return
	 */
	public String colorize(String msg)
    {
        String coloredMsg = "";
        for(int i = 0; i < msg.length(); i++)
        {
            if(msg.charAt(i) == '&')
                coloredMsg += '§';//§
            else
                coloredMsg += msg.charAt(i);
        }
        return coloredMsg;
    }
	
	private void replaceVariables(CommandSender sender, List<String> strings){
		for(int i = 0;i < strings.size(); i++){
			String str = strings.get(i);
			if(str == null) continue;
			
			str = colorize(str);
			
			if(str.contains("${")){
				int start = -1;
				int end = -1;
				
				while(!((start = str.indexOf("${")) == -1 || 
						(end = str.indexOf("}")) == -1)){
					
					String leftStr = str.substring(0, start);
					String rightStr = str.substring(end + 1, str.length());
					
					String varName = str.substring(start + 2, end);
					
					switch(varName){
					case "double":
						str = leftStr+String.valueOf(this.doub.poll())+rightStr;
						break;
					case "integer":
						str = leftStr+String.valueOf(this.integer.poll())+rightStr;
						break;
					case "string":
						str = leftStr+String.valueOf(this.string.poll())+rightStr;
						break;
					case "bool":
						str = leftStr+String.valueOf(this.bool.poll())+rightStr;
						break;
					case "player":
						str = leftStr + (sender == null ? "null" : sender.getName()) + rightStr;
						break;
					case "maincommand":
						str = leftStr + base.executor.mainCommand + rightStr;
						break;
					default:
						str = leftStr + String.valueOf("[?]") + rightStr;
						break;
					}
				}	
			}
			
			strings.set(i, str);
		}
		
		this.doub.clear();
		this.integer.clear();
		this.string.clear();
		this.bool.clear();
	}
	
	/**
	 * Create enum class and implement this interface. See {@link DefaultLanguages} for example.
	 * @author wysohn
	 *
	 */
	public interface Language{
		public String[] getEngDefault();
	}
	
	private class LanguageFileSession {
		private File file;
		FileConfiguration config;
		public LanguageFileSession(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
			this.file = file;
			
			if(!file.exists())
				file.createNewFile();
			
			this.config = new Utf8YamlConfiguration();
			
			this.config.load(file);
		}
		
		public void reload() throws FileNotFoundException, IOException, InvalidConfigurationException{
			this.config.load(file);
		}
		
		public void save() throws IOException{
			this.config.save(file);
		}
	}

}
