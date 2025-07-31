package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game of Uno.
 * This class manages the game logic and interactions between players, deck, and the table.
 */
public class GameUno implements IGameUno {

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;

    private static GameUno instance;

    private List<GameObserver> observers = new ArrayList<>();
    private Player currentPlayer;
    private Card currentCard;

    private boolean gameEnded;

    /**
     * Constructs a new GameUno instance.
     *
     * @param humanPlayer   The human player participating in the game.
     * @param machinePlayer The machine player participating in the game.
     * @param deck          The deck of cards used in the game.
     * @param table         The table where cards are placed during the game.
     */

    public GameUno(Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
    }

    /**
     * Starts the Uno game by distributing cards to players.
     * The human player and the machine player each receive 10 cards from the deck.
     */
    @Override
    public void startGame() {
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                humanPlayer.addCard(this.deck.takeCard());
            } else {
                machinePlayer.addCard(this.deck.takeCard());
            }
        }
        System.out.println("entro al starGame.");
        //notifyGameStarted();
    }

    public static GameUno getInstance() {
        if (instance == null) {
            // Inicializa con valores por defecto
            Player humanPlayer = new Player("HUMAN_PLAYER");
            Player machinePlayer = new Player("MACHINE_PLAYER");
            Deck deck = new Deck();
            Table table = new Table();
            instance = new GameUno(humanPlayer, machinePlayer, deck, table);
        }
        return instance;
    }


    /**
     * Allows a player to draw a specified number of cards from the deck.
     *
     * @param player        The player who will draw cards.
     * @param numberOfCards The number of cards to draw.
     */
    @Override
    public void eatCard(Player player, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            player.addCard(this.deck.takeCard());
        }
    }

    /**
     * Places a card on the table during the game.
     *
     * @param card The card to be placed on the table.
     */
    @Override
    public void playCard(Card card) {
        this.table.addCardOnTheTable(card);
        notifyCardPlayed(card);
    }

    // Métodos para manejar observadores
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    // Notificaciones específicas
    public void notifyCardPlayed(Card card) {
        observers.forEach(obs -> obs.onCardPlayed(card));
    }

    private void notifyPlayerChanged(Player player) {
        observers.forEach(obs -> obs.onPlayerChanged(player));
    }

    private void notifyGameStarted() {
        observers.forEach(GameObserver::onGameStarted);
    }

    private void notifyCardDrawn(Player player, Card card) {
        observers.forEach(obs -> obs.onCardDrawn(player, card));
    }

    private void notifyGameEnded(Player winner) {
        observers.forEach(obs -> obs.onGameEnded(winner));
    }


    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     *
     * @param playerWhoSang The player who shouted "Uno".
     */
    @Override
    public void haveSungOne(String playerWhoSang) {
        if (playerWhoSang.equals("HUMAN_PLAYER")) {
            machinePlayer.addCard(this.deck.takeCard());
        } else {
            humanPlayer.addCard(this.deck.takeCard());
        }
    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow The initial position of the cards to show.
     * @return An array of cards visible to the human player.
     */
    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        int totalCards = this.humanPlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.humanPlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }

    public Card[] getCurrentVisibleCardsMachinePlayer(int posInitCardToShow) {
        int totalCards = this.machinePlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.machinePlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }

    /**
     *
     * Check every card of the player to determine if a card cannot be taken from the deck
     *
     * @param player a player (machine or human player)
     * @return  boolean of whether the player must take from the deck or not
     * */
    public boolean mustDrawFromDeck(Player player) {

        for( int i = 0; i < player.getCardsPlayer().size(); i++) {
            if(table.canAddCardTable(player.getCard(i))){return false;}
        }

        return true;
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the deck is empty, indicating the game is over; otherwise, false.
     */
    @Override
    public Boolean isGameOver() {return gameEnded;}

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public void endGame(Player winner) {
        setGameEnded(true);
        notifyGameEnded(winner); // Añade esta línea
    }

    public Player getHumanPlayer() {
        return this.humanPlayer;
    }

    public Player getMachinePlayer() {
        return this.machinePlayer;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public Table getTable() {
        return this.table;
    }


}
