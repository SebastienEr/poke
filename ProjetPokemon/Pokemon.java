public class Pokemon {
    private String name;
    private int maxHp;
    private int hp;
    private int attack;
    private int xp;
    private int level;

    public Pokemon(String name, int maxHp, int attack) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.xp = 0;
        this.level = 1;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) {
            hp = maxHp; 
        }
    }

    public void gainXp(int amount) {
        xp += amount;
        if (xp >= level * 100) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        xp = 0;
        maxHp += 10;
        hp = maxHp;
        attack += 5;
    }
}
