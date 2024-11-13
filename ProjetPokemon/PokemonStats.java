package poke.ProjetPokemon;

import java.util.Random;

public class PokemonStats {
    public static final Pokemon SALAMECHE = new Pokemon("Salameche", 100, 200);
    public static final Pokemon BULBIZARRE = new Pokemon("Bulbizarre", 120, 15);
    public static final Pokemon CARAPUCE = new Pokemon("Carapuce", 110, 20);
    public static final Pokemon PIKACHU = new Pokemon("Pikachu", 90, 25);
    public static final Pokemon RATTATA = new Pokemon("Rattata", 70, 15);
    public static final Pokemon SULFURA = new Pokemon("Sulfura", 150, 50);
    public static final Pokemon MEW = new Pokemon("Mew", 200, 60);
    public static final Pokemon LEVIATOR = new Pokemon("Leviator", 180, 55);
    public static final Pokemon ABO = new Pokemon("Abo", 75, 20);
    public static final Pokemon ASPICOT = new Pokemon("Aspicot", 60, 10);
    public static final Pokemon CHENIPAN = new Pokemon("Chenipan", 65, 12);

    private static final Pokemon[] POKEMONS = {SALAMECHE, BULBIZARRE, CARAPUCE, PIKACHU, RATTATA, SULFURA, MEW, LEVIATOR, ABO, ASPICOT, CHENIPAN};

    public static Pokemon getRandomPokemon() {
        Random random = new Random();
        return POKEMONS[random.nextInt(POKEMONS.length)];
    }

    public static Pokemon getPlayerPokemon() {
        // Return the player's Pokemon, for now, we return SALAMECHE
        return SALAMECHE;
    }
}
