/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rotp.model.ai;

public class FleetStats {
    public float avgShield;
    public float avgDefense;
    public float avgMissileDefense;
    public float totalHP;
    public float avgSpecials;
    public float avgCombatSpeed;
    public FleetStats() {
       avgShield = 0;
       avgDefense = 0;
       avgMissileDefense = 0;
       totalHP = 0;
       avgSpecials = 0;
       avgCombatSpeed = 0;
    }
    public void merge(FleetStats statsToMerge) {
        float newHP = totalHP + statsToMerge.totalHP;
        avgShield = (avgShield * totalHP + statsToMerge.avgShield * statsToMerge.totalHP) / newHP;
        avgDefense = (avgDefense * totalHP + statsToMerge.avgDefense * statsToMerge.totalHP) / newHP;
        avgMissileDefense = (avgMissileDefense * totalHP + statsToMerge.avgMissileDefense * statsToMerge.totalHP) / newHP;
        avgSpecials = (avgSpecials * totalHP + statsToMerge.avgSpecials * statsToMerge.totalHP) / newHP;
        avgCombatSpeed = (avgCombatSpeed * totalHP + statsToMerge.avgCombatSpeed * statsToMerge.totalHP) / newHP;
        totalHP = newHP;
    }
}