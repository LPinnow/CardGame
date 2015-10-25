package controller;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;

/**
 * This class represents the menu bar for the application.
 */
public class GameMenu extends MenuBar {

  /**
   * Constructs a {@link GameMenu} object for the given {@link CardGameMain}.
   *
   * @param cardGameApp The {@link CardGameMain} instance to create the menu for.
   */
  public GameMenu(CardGameMain cardGameApp) {
    Menu gameMenu = new Menu("Game");
    Menu settingsMenu = new Menu("Settings");
    getMenus().addAll(gameMenu, settingsMenu);

    MenuItem newGameMenuItem = new MenuItem("New Game");

    MenuItem exitGameMenuItem = new MenuItem("Exit");
    exitGameMenuItem.setOnAction(e -> Platform.exit());

    gameMenu.getItems().addAll(newGameMenuItem, exitGameMenuItem);

    // card themes
    Menu cardThemeSettingsMenu = new Menu("Select card theme");
    ToggleGroup cardThemeToggleGroup = new ToggleGroup();

    RadioMenuItem classicMenuItem = new RadioMenuItem("Classic");
    classicMenuItem.setToggleGroup(cardThemeToggleGroup);
    classicMenuItem.setOnAction(e -> {
      cardGameApp.cardTheme.setThemeFile("/cardfaces/classic/theme.json");
      cardGameApp.gameBoard.updateCardViews(cardGameApp.cardTheme);
    });

    RadioMenuItem piatnikImperialMenuItem = new RadioMenuItem("Piatnik Imperial");
    piatnikImperialMenuItem.setToggleGroup(cardThemeToggleGroup);
    piatnikImperialMenuItem.setOnAction(e -> {
      cardGameApp.cardTheme.setThemeFile("/cardfaces/piatnik_imperial/theme.json");
      cardGameApp.gameBoard.updateCardViews(cardGameApp.cardTheme);
    });

    RadioMenuItem piatnikLuxuryMenuItem = new RadioMenuItem("Piatnik Luxury");
    piatnikLuxuryMenuItem.setToggleGroup(cardThemeToggleGroup);
    piatnikLuxuryMenuItem.setOnAction(e -> {
      cardGameApp.cardTheme.setThemeFile("/cardfaces/piatnik_luxury/theme.json");
      cardGameApp.gameBoard.updateCardViews(cardGameApp.cardTheme);
    });

    cardThemeSettingsMenu.getItems().addAll(classicMenuItem,
        piatnikImperialMenuItem, piatnikLuxuryMenuItem);

    cardThemeToggleGroup.selectToggle(cardThemeToggleGroup.getToggles().get(0));

    // card backs
    Menu cardBackSettingsMenu = new Menu("Select card back");
    ToggleGroup cardBackToggleGroup = new ToggleGroup();

    RadioMenuItem classicBlueMenuItem = new RadioMenuItem("Classic blue");
    classicBlueMenuItem.setToggleGroup(cardBackToggleGroup);
    classicBlueMenuItem.setOnAction(e -> {
      cardGameApp.cardTheme.setBackFace(new Image("/backfaces/bb.png"));
      cardGameApp.gameBoard.updateCardViews(cardGameApp.cardTheme);
    });

    RadioMenuItem hearthStoneMenuItem = new RadioMenuItem("Hearthstone");
    hearthStoneMenuItem.setToggleGroup(cardBackToggleGroup);
    hearthStoneMenuItem.setOnAction(e -> {
      cardGameApp.cardTheme.setBackFace(new Image("/backfaces/hearthstone.png"));
      cardGameApp.gameBoard.updateCardViews(cardGameApp.cardTheme);
    });

    RadioMenuItem piatnikImperialBack1 = new RadioMenuItem("Piatnik Imperial 1");
    piatnikImperialBack1.setToggleGroup(cardBackToggleGroup);
    piatnikImperialBack1.setOnAction(e -> {
      cardGameApp.cardTheme.setBackFace(new Image("/backfaces/piatnik_imperial_1.png"));
      cardGameApp.gameBoard.updateCardViews(cardGameApp.cardTheme);
    });

    RadioMenuItem piatnikImperialBack2 = new RadioMenuItem("Piatnik Imperial 2");
    piatnikImperialBack2.setToggleGroup(cardBackToggleGroup);
    piatnikImperialBack2.setOnAction(e -> {
      cardGameApp.cardTheme.setBackFace(new Image("/backfaces/piatnik_imperial_2.png"));
      cardGameApp.gameBoard.updateCardViews(cardGameApp.cardTheme);
    });

    RadioMenuItem piatnikLuxuryBack1 = new RadioMenuItem("Piatnik Luxury 1");
    piatnikLuxuryBack1.setToggleGroup(cardBackToggleGroup);
    piatnikLuxuryBack1.setOnAction(e -> {
      cardGameApp.cardTheme.setBackFace(new Image("/backfaces/piatnik_luxury_1.png"));
      cardGameApp.gameBoard.updateCardViews(cardGameApp.cardTheme);
    });

    RadioMenuItem piatnikLuxuryBack2 = new RadioMenuItem("Piatnik Luxury 2");
    piatnikLuxuryBack2.setToggleGroup(cardBackToggleGroup);
    piatnikLuxuryBack2.setOnAction(e -> {
      cardGameApp.cardTheme.setBackFace(new Image("/backfaces/piatnik_luxury_2.png"));
      cardGameApp.gameBoard.updateCardViews(cardGameApp.cardTheme);
    });

    cardBackSettingsMenu.getItems().addAll(classicBlueMenuItem,
        hearthStoneMenuItem, piatnikImperialBack1, piatnikImperialBack2,
        piatnikLuxuryBack1, piatnikLuxuryBack2);

    cardBackToggleGroup.selectToggle(cardBackToggleGroup.getToggles().get(0));

    // table backgrounds
    Menu tableBackgroundsMenu = new Menu("Select table background");
    ToggleGroup tableToggleGroup = new ToggleGroup();

    RadioMenuItem greenFeltBGItem = new RadioMenuItem("Green felt");
    greenFeltBGItem.setToggleGroup(tableToggleGroup);
    greenFeltBGItem.setOnAction(e ->
        cardGameApp.gameBoard.setTableBackground(new Image("/tableaous/green-felt.png")));

    RadioMenuItem woodBGItem = new RadioMenuItem("Wood desk");
    woodBGItem.setToggleGroup(tableToggleGroup);
    woodBGItem.setOnAction(e ->
        cardGameApp.gameBoard.setTableBackground(new Image("/tableaous/wood.jpg")));

    tableBackgroundsMenu.getItems().addAll(greenFeltBGItem, woodBGItem);

    tableToggleGroup.selectToggle(tableToggleGroup.getToggles().get(0));

    settingsMenu.getItems().addAll(cardThemeSettingsMenu, cardBackSettingsMenu,
        tableBackgroundsMenu);
  }
}
