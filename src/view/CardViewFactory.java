package view;

import model.card.Card;

/**
 * Factory class for creating {@link CardView} objects.
 * 
 * This class has code based upon the following project:
 * Zoltan Dalmadi, "JCardGamesFX", 2015, GitHub repository, github.com/ZoltanDalmadi/JCardGamesFX.
 */
public class CardViewFactory {

  /**
   * Actual theme of the cards.
   */
  private static CardTheme cardTheme;

  /**
   * Returns the active theme of the cards.
   *
   * @return The {@link CardTheme} object.
   */
  public static CardTheme getCardTheme() {
    return cardTheme;
  }

  /**
   * Sets the active theme of the cards.
   *
   * @param cardTheme The {@link CardTheme} object to be set.
   */
  public static void setCardTheme(CardTheme cardTheme) {
    CardViewFactory.cardTheme = cardTheme;
  }

  /**
   * Creates a {@link CardView} object for the specified
   * {@link Card} object.
   *
   * @param card The {@link Card} object to create the view for.
   * @return The created {@link CardView} object.
   */
  public static CardView createCardView(Card card) {
    CardView result = new CardView(card.isFaceDown());

    result.setFrontFace(cardTheme.getFrontFace(card.getId()));
    result.setBackFace(cardTheme.getBackFace());
    result.setShortID(card.getId());

    return result;
  }

}
