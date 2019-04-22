package com.gestankbratwurst.PowerTurrets.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.gestankbratwurst.PowerTurrets.fileIO.Language;

@AllArgsConstructor
public enum AmmoType {
	
	SHELL(Language.AMMUNITION_TYPE_SHELL.toChatString()), BOX(Language.AMMUNITION_TYPE_SHELL.toChatString());
	
	@Getter
	private final String displayName;
	@Getter
	private static final String nbtKey = "ammoType";
}
