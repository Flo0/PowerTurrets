package com.gestankbratwurst.PowerTurrets.util;

import com.gestankbratwurst.PowerTurrets.fileIO.Language;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FireRate {
	
	RAPID(5, 8, Language.FIRE_RATE_RAPID),
	VERY_FAST(10, 7, Language.FIRE_RATE_VERY_FAST),
	FAST(15, 6, Language.FIRE_RATE_FAST),
	NORMAL(20, 5, Language.FIRE_RATE_NORMAL),
	MODERATED(25, 4, Language.FIRE_RATE_MODERATED),
	SLOWER(30, 3, Language.FIRE_RATE_SLOWER),
	SLOW(35, 2, Language.FIRE_RATE_SLOW),
	VERY_SLOW(40, 1, Language.FIRE_RATE_VERY_SLOW),
	LETHARGIC(60, 0, Language.FIRE_RATE_LETHARGIC);
	
	@Getter
	private long period;
	@Getter
	private int value;
	@Getter
	private Language name;
	
	public FireRate getNext() {
		switch(this) {
		case LETHARGIC: return VERY_SLOW;
		case VERY_SLOW: return SLOW;
		case SLOW: return SLOWER;
		case SLOWER: return MODERATED;
		case MODERATED: return NORMAL;
		case NORMAL: return FAST;
		case FAST: return VERY_FAST;
		case VERY_FAST: return RAPID;
		case RAPID: return null;
		default: return LETHARGIC;
		}
	}
}
