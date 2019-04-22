package com.gestankbratwurst.PowerTurrets.util;

import com.gestankbratwurst.PowerTurrets.fileIO.Language;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ProjectileVelocity {
	
	SLOW(0.5, 1, 0, Language.PROJECTILE_VELOCITY_SLOW),
	NORMAL(1.0, 1, 1, Language.PROJECTILE_VELOCITY_NORMAL),
	FAST(1.5, 1, 2, Language.PROJECTILE_VELOCITY_FAST),
	VERY_FAST(1.5, 2, 3, Language.PROJECTILE_VELOCITY_VERY_FAST),
	SUDDEN_IMPACT(1.0, 1, 4, Language.PROJECTILE_VELOCITY_SUDDEN_IMPACT);
	
	@Getter
	private double stepMultiplier;
	@Getter
	private int stepJumper;
	@Getter
	private int value;
	@Getter
	private final Language name;
	
	public ProjectileVelocity getNext() {
		switch(this) {
		case SLOW: return NORMAL;
		case NORMAL: return FAST;
		case FAST: return VERY_FAST;
		case VERY_FAST: return SUDDEN_IMPACT;
		case SUDDEN_IMPACT: return null;
		default: return SLOW;
		}
	}
	
}
