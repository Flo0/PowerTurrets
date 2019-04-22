package com.gestankbratwurst.PowerTurrets.Threads;

import org.bukkit.Bukkit;

import com.gestankbratwurst.PowerTurrets.Core;
import com.gestankbratwurst.PowerTurrets.projectiles.TurretProjectile;
import com.gestankbratwurst.PowerTurrets.util.ProjectileVelocity;

public class ProjectilePathfinder implements Runnable{
	
	public ProjectilePathfinder(TurretProjectile projectile) {
		this.projectile = projectile;
		this.id = Bukkit.getScheduler().runTaskTimer(Core.getPlugin(), this, 0, 1).getTaskId();
	}
	
	private final TurretProjectile projectile;
	private final int id;
	
	@Override
	public void run() {
		
		int count = 0;
		
		if(this.projectile.getVelocity().equals(ProjectileVelocity.SUDDEN_IMPACT)) {
			boolean end = false;
			while(!end) {
				end = this.projectile.tickBullet();
				if(end) break;
				end = !this.projectile.updateState();
			}
			this.stop();
			return;
		}
		
		while(count < this.projectile.getVelocity().getStepJumper()) {
			if(this.projectile.tickBullet()) {
				this.stop();
				return;
			}
			
			if(!this.projectile.updateState()) this.stop();
			count++;
		}
		
	}
	
	private void stop() {
		Bukkit.getScheduler().cancelTask(this.id);
	}
	
}
