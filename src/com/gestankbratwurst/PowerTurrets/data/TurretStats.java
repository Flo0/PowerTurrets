package com.gestankbratwurst.PowerTurrets.data;

import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import com.gestankbratwurst.PowerTurrets.Core;
import com.gestankbratwurst.PowerTurrets.util.BulletImpact;
import com.gestankbratwurst.PowerTurrets.util.FireRate;
import com.gestankbratwurst.PowerTurrets.util.ProjectileVelocity;
import com.gestankbratwurst.PowerTurrets.util.TurretType;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.Getter;
import lombok.Setter;
import net.crytec.shaded.parsii.eval.Expression;
import net.crytec.shaded.parsii.eval.Parser;
import net.crytec.shaded.parsii.eval.Scope;
import net.crytec.shaded.parsii.eval.Variable;
import net.crytec.shaded.parsii.tokenizer.ParseException;

public class TurretStats {
	
	public TurretStats(TurretType type) {
		FileConfiguration config = Core.getPlugin().getFileManager().getMainConfig();
		String section = type.getConfigPath() + ".Stats";
		this.type = type;
		this.kills = Maps.newHashMap();
		this.currentExp = 0;
		this.level = 0;
		this.hp = config.getDouble(section + ".Health.BaseValue");
		this.fireRate = FireRate.valueOf(config.getString(section + ".FireRate.BaseValue"));
		this.baseDamage = config.getDouble(section + ".BaseDamage.BaseValue");
		this.velocity = ProjectileVelocity.valueOf(config.getString(section + ".ProjectileVelocity.BaseValue"));
		this.accuracy = (float) config.getDouble(section + ".BaseAccuracy.BaseValue");
		this.range = config.getDouble(section + ".Range.BaseValue");
		this.impactMechanics = Sets.newHashSet();
		
		this.scope = new Scope();
		this.lvlVar = scope.getVariable("x");
		
		try {
			this.levelUpCostFormular = Parser.parse(config.getString(type.getConfigPath() + ".LevelUpFormular"), this.scope);
			this.pointsPerLevelFormular = Parser.parse(config.getString(type.getConfigPath() + ".PointsPerLevelFormular"), this.scope);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		this.points = 0;
		this.nextLevelCost = 0;
		this.nextLevelPointGain = 0;
		this.currentHP = this.hp;
		
		this.maxlevel = config.getInt(type.getConfigPath() + ".MaxLevel");
		
		this.hpLvlCost = config.getInt(section + ".Health.LevelCost");
		this.damageLvlCost = config.getInt(section + ".BaseDamage.LevelCost");
		this.fireRateLvlCost = config.getInt(section + ".FireRate.LevelCost");
		this.velocityLvlCost = config.getInt(section + ".ProjectileVelocity.LevelCost");
		this.accuracyLvlCost = config.getInt(section + ".BaseAccuracy.LevelCost");
		this.rangeLvlCost = config.getInt(section + ".Range.LevelCost");
		
		this.hpValueGain = config.getDouble(section + ".Health.ValueGain");
		this.damageValueGain = config.getDouble(section + ".BaseDamage.ValueGain");
		this.accuracyValueGain = config.getDouble(section + ".BaseAccuracy.ValueGain");
		this.rangeValueGain = config.getDouble(section + ".Range.ValueGain");
		
		this.maxHp = config.getDouble(section + ".Health.MaxValue");
		this.maxDamage = config.getDouble(section + ".BaseDamage.MaxValue");
		this.maxFireRate = FireRate.valueOf(config.getString(section + ".FireRate.MaxValue"));
		this.maxVelocity = ProjectileVelocity.valueOf(config.getString(section + ".ProjectileVelocity.MaxValue"));
		this.maxAccuracy = config.getDouble(section + ".BaseAccuracy.MaxValue");
		this.maxRange = config.getDouble(section + ".Range.MaxValue");
		
		this.levelUp();
	}
	
	@Getter
	private final TurretType type;
	
	@Getter
	private Map<EntityType, Integer> kills;
	@Getter @Setter
	private int currentExp;
	@Getter
	private int level;
	@Getter
	private final int maxlevel;
	@Getter
	private int points;
	
	@Getter
	private double hp;
	@Getter @Setter
	private double currentHP;
	
	@Getter @Setter
	private FireRate fireRate;
	@Getter @Setter
	private double baseDamage;
	@Getter @Setter
	private ProjectileVelocity velocity;
	@Getter @Setter
	private float accuracy;
	@Getter @Setter
	private double range;
	
	@Getter
	private Set<BulletImpact> impactMechanics;
	
	private final Scope scope;
	private final Variable lvlVar;

	private Expression levelUpCostFormular;
	private Expression pointsPerLevelFormular;
	
	@Getter
	private int nextLevelCost;
	@Getter
	private int nextLevelPointGain;
	
	@Getter
	private final int hpLvlCost;
	@Getter
	private final int damageLvlCost;
	@Getter
	private final int fireRateLvlCost;
	@Getter
	private final int velocityLvlCost;
	@Getter
	private final int accuracyLvlCost;
	@Getter
	private final int rangeLvlCost;
	
	@Getter
	private final double hpValueGain;
	@Getter
	private final double damageValueGain;
	@Getter
	private final double accuracyValueGain;
	@Getter
	private final double rangeValueGain;
	
	@Getter
	private final double maxHp;
	@Getter
	private final double maxDamage;
	@Getter
	private final double maxAccuracy;
	@Getter
	private final double maxRange;
	@Getter
	private final ProjectileVelocity maxVelocity;
	@Getter
	private final FireRate maxFireRate;
	
	public boolean lvlUpHealth() {
		if(this.points < this.hpLvlCost) return false;
		if(this.hp + this.hpValueGain > this.maxHp) return false;
		this.points -= this.hpLvlCost;
		this.hp += this.hpValueGain;
		return true;
	}
	
	public boolean lvlUpDamage() {
		if(this.points < this.damageLvlCost) return false;
		if(this.baseDamage + this.damageValueGain > this.maxDamage) return false;
		this.points -= this.damageLvlCost;
		this.baseDamage += this.damageValueGain;
		return true;
	}
	
	public boolean lvlFireRate() {
		if(this.points < this.fireRateLvlCost) return false;
		FireRate nextRate = this.fireRate.getNext();
		if(nextRate==null) return false;
		if(nextRate.getValue() > this.maxFireRate.getValue()) return false;
		this.points -= this.fireRateLvlCost;
		this.fireRate = nextRate;
		return true;
	}
	
	public boolean lvlProjectileVelocity() {
		if(this.points < this.velocityLvlCost) return false;
		ProjectileVelocity nextVel = this.velocity.getNext();
		if(nextVel==null) return false;
		if(nextVel.getValue() > this.maxVelocity.getValue()) return false;
		this.points -= this.velocityLvlCost;
		this.velocity = nextVel;
		return true;
	}
	
	public boolean lvlUpAccuracy() {
		if(this.points < this.accuracyLvlCost) return false;
		if(this.accuracy + this.accuracyValueGain > this.maxAccuracy) return false;
		this.points -= this.accuracyLvlCost;
		this.accuracy += this.accuracyValueGain;
		return true;
	}
	
	public boolean lvlUpRange() {
		if(this.points < this.rangeLvlCost) return false;
		if(this.range + this.rangeValueGain > this.maxRange) return false;
		this.points -= this.rangeLvlCost;
		this.range += this.range;
		return true;
	}

	
	public void levelUp() {
		this.level++;
		this.points += this.nextLevelPointGain;
		this.lvlVar.setValue(this.level);
		this.nextLevelCost = (int) this.levelUpCostFormular.evaluate();
		this.nextLevelPointGain = (int) this.pointsPerLevelFormular.evaluate();
	}
}
