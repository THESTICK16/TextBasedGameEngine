import java.awt.*;

public class Player {

    private int hp;

    private int damageBound;

    private int strength; //for an attack, randomly generates number, adds respective stat, compares to armor class
    //use item damage to determine, add respective stat?
    private int magic; //compared to armor class

    private int dexterity;

    private int armorClass; //dexterity, armor wearing, etc... 10+defBonus(armor)+dexterity

    //These affect the level up gains for Strength, Dexterity, and Magic
    private int meleeHits;

    private int rangedHits;

    private int magicHits;

    //This is for the new morale system.
    private int morale = 50;

    private int normalArmorClass;

    private int acAffectedArmorClass;
    public int maxHP;

    private int gold;

    private int enemyDifficulty;


    public Player(){
        maxHP = 20;
        gold = 100;
        armorClass = 0;
        enemyDifficulty = -1;
    }

    public Player(int hp, int damageBound){
        this.hp=hp;
        this.damageBound=damageBound;
    }

    public Player(int enemyDifficulty){
        this.enemyDifficulty = enemyDifficulty;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getGold() { return gold; }

    public void setGold(int gold) { this.gold = gold;}

    public int getEnemyDifficulty() { return enemyDifficulty; }

    public void setEnemyDifficulty(int enemyDifficulty) { this.enemyDifficulty = enemyDifficulty; }

    public int getDamageBound() {
        return damageBound;
    }

    public void setDamageBound(int damageBound) {
        this.damageBound = damageBound;
    }

    public int calculateDamage(){
        return (int)(Math.random() * damageBound) + 1;
    }

    public int defendProtection() {
        return (int)(Math.random() + (armorClass/4)) + 1; //FIXME: Find balance for
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    public void addArmor(int armor){
        this.armorClass = 10 + this.dexterity + armor;
    }

    public int strengthAttack(){
        return (int)(Math.random()*20) + strength;
    }

    public int dexterityAttack(){
        return (int)(Math.random()*20) + dexterity;
    }

    public int magicAttack(){
        return (int)(Math.random()*20) + magic;
    }

    public int defend() {
        return 0;
    }

    public void setMeleeHits(int meleeHits) {this.meleeHits = meleeHits;}

    public int getMeleeHits() {return meleeHits;}
    public void setRangedHits(int rangedHits) {this.rangedHits = rangedHits;}
    public int getRangedHits() {return rangedHits;}
    public void setMagicHits(int magicHits) {this.magicHits = magicHits;}
    public int getMagicHits() {return magicHits;}
    public int getMorale() {return morale;}
    public void setMorale(int morale) {this.morale = morale;}
    public int getNormalArmorClass() {return normalArmorClass;}
    public void setNormalArmorClass(int normalArmorClass) {this.normalArmorClass = normalArmorClass;}
    public void setAcAffectedArmorClass() {
        if (morale > 40 && morale < 60) {
            acAffectedArmorClass = normalArmorClass;
        }
        else if (morale >= 60 && morale < 70) {
            acAffectedArmorClass = normalArmorClass + 2;
        }
        else if (morale >= 70) {
            acAffectedArmorClass = normalArmorClass + 4;
        }
        else if (morale <= 40 && morale > 30) {
            acAffectedArmorClass = normalArmorClass - 2;
        }
        else {
            acAffectedArmorClass = normalArmorClass - 4;
        }
    }
    public int getAcAffectedArmorClass() {return acAffectedArmorClass;}

}