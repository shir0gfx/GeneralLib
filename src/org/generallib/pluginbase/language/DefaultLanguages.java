package org.generallib.pluginbase.language;

import org.generallib.pluginbase.PluginLanguage.Language;

public enum DefaultLanguages implements Language {
	Plugin_NotEnabled("Plugin is not enabled. "),
	Plugin_SetEnableToTrue("Please check your setting at config.yml to make sure it's enabled."),
	Plugin_WillBeDisabled("Plugin will be disabled."), 
	
	General_NotANumber("&c${string} is not a number!"),
	General_OutOfBound("&c${string} is out of bound!"),
	General_OutOfBound_RangeIs("&crange: &6${integer} &7< &fvalue &7< &6${integer}"),	
	General_Header("&7======== &6${string}&7 ========"), 
	General_InvalidType("&c${string} is not a valid type!"),
	General_NoSuchPlayer("&cNo such player named ${string}!"), 
	General_NoSuchCommand("&cNo such command ${string}!"), 
	General_Allow("&aAllow"), 
	General_Deny("&cDeny"),
	General_NotABoolean("&c${string} is not a boolean!"),
	General_NotEnoughPermission("&cYou don't have enough permission!"),
	
	Economy_NotEnoughMoney("&cNot enough money! Required:[&6${double}&c]"), 
	Economy_TookMoney("&aTook [&6${double}&a] from your account!"),
	
	Command_Format_Description("&6/${maincommand} ${string} &5- &7${string}"),
	Command_Format_Usage("&cusages:", "  /${maincommand} ${string} ${string}"),
	
	Command_Help_PageDescription("&6Page &7${integer}/${integer}"),
	Command_Help_TypeHelpToSeeMore("&6Type &6/${maincommand} help &7<page> &6to see next pages."),
	Command_Help_Description("Show all commands and its desriptions of this plugin."), 
	Command_Help_Usage("<page> for page to see."),
	;
	
	private final String[] englishDefault;
	private DefaultLanguages(String... englishDefault){
		this.englishDefault = englishDefault;
	}
	
	@Override
	public String[] getEngDefault() {
		return englishDefault;
	}	
}
