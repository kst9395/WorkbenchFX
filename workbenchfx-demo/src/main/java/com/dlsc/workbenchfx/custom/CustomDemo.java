package com.dlsc.workbenchfx.custom;

import static com.dlsc.workbenchfx.WorkbenchFx.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.custom.calendar.CalendarModule;
import com.dlsc.workbenchfx.custom.notes.NotesModule;
import com.dlsc.workbenchfx.custom.overlay.CustomOverlay;
import com.dlsc.workbenchfx.custom.preferences.PreferencesModule;
import com.dlsc.workbenchfx.custom.test.DropdownTestModule;
import com.dlsc.workbenchfx.custom.test.NavigationDrawerTestModule;
import com.dlsc.workbenchfx.custom.test.WidgetsTestModule;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import com.dlsc.workbenchfx.view.controls.NavigationDrawer;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.function.BiFunction;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomDemo extends Application {

  private static final Logger LOGGER = LogManager.getLogger(CustomDemo.class.getName());
  public WorkbenchFx workbenchFx;

  BiFunction<WorkbenchFx, Module, Node> tabFactory = (workbench, module) -> {
    TabControl tabControl = new TabControl(module);
    workbench.activeModuleProperty().addListener((observable, oldValue, newValue) -> {
      LOGGER.trace("Tab Factory - Old Module: " + oldValue);
      LOGGER.trace("Tab Factory - New Module: " + oldValue);
      if (module == newValue) {
        tabControl.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
        LOGGER.error("STYLE SET");
      }
      if (module == oldValue) {
        // switch from this to other tab
        tabControl.getStyleClass().remove(STYLE_CLASS_ACTIVE_TAB);
      }
    });
    tabControl.setOnClose(e -> workbench.closeModule(module));
    tabControl.setOnActive(e -> workbench.openModule(module));
    tabControl.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
    System.out.println("This tab was proudly created by SteffiFX");
    return tabControl;
  };

  BiFunction<WorkbenchFx, Module, Node> tileFactory = (workbench, module) -> {
    TileControl tileControl = new TileControl(module);
    tileControl.setOnActive(e -> workbench.openModule(module));
    System.out.println("This tile was proudly created by SteffiFX");
    return tileControl;
  };
  BiFunction<WorkbenchFx, Integer, Node> pageFactory = (workbench, pageIndex) -> {
    final int COLUMNS_PER_ROW = 2;

    GridPane gridPane = new GridPane();
    gridPane.getStyleClass().add("tile-page");

    int position = pageIndex * workbench.modulesPerPage;
    int count = 0;
    int column = 0;
    int row = 0;

    while (count < workbench.modulesPerPage && position < workbench.getModules().size()) {
      Module module = workbench.getModules().get(position);
      Node tile = workbench.getTile(module);
      gridPane.add(tile, column, row);

      position++;
      count++;
      column++;

      if (column == COLUMNS_PER_ROW) {
        column = 0;
        row++;
      }
    }
    return gridPane;
  };
  private Callback<WorkbenchFx, Node> navigationDrawerFactory = workbench -> {
    NavigationDrawer navigationDrawer = new NavigationDrawer(workbench);
    StackPane.setAlignment(navigationDrawer, Pos.TOP_LEFT);
    navigationDrawer.maxWidthProperty().bind(workbench.widthProperty().multiply(.5));
    return navigationDrawer;
  };

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Scene myScene = new Scene(initWorkbench());

    primaryStage.setTitle("Custom WorkbenchFX Demo");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }

  private WorkbenchFx initWorkbench() {
    // Navigation Drawer
    Menu menu1 = new Menu("Customer", createIcon(FontAwesomeIcon.USER));
    Menu menu2 = new Menu("Tariff Management", createIcon(FontAwesomeIcon.BUILDING));
    Menu menu3 = new Menu("Complaints", createIcon(FontAwesomeIcon.BOMB));

    FontAwesomeIcon genericIcon = FontAwesomeIcon.QUESTION;
    MenuItem item11 = new MenuItem("Item 1.1", createIcon(genericIcon));
    MenuItem item12 = new MenuItem("Item 1.2", createIcon(genericIcon));
    MenuItem item13 = new MenuItem("Item 1.3", createIcon(genericIcon));
    MenuItem item14 = new MenuItem("Item 1.4", createIcon(genericIcon));

    Menu item21 = new Menu("Item 2.1", createIcon(genericIcon));
    MenuItem item22 = new MenuItem("Item 2.2", createIcon(genericIcon));

    MenuItem item211 = new MenuItem("Item 2.1.1", createIcon(genericIcon));
    MenuItem item212 = new MenuItem("Item 2.1.2", createIcon(genericIcon));
    MenuItem item213 = new MenuItem("Item 2.1.3", createIcon(genericIcon));
    MenuItem item214 = new MenuItem("Item 2.1.4", createIcon(genericIcon));
    MenuItem item215 = new MenuItem("Item 2.1.5", createIcon(genericIcon));

    MenuItem item31 = new MenuItem("Item 3.1", createIcon(genericIcon));
    MenuItem item32 = new MenuItem("Item 3.2", createIcon(genericIcon));
    MenuItem item33 = new MenuItem("Item 3.3", createIcon(genericIcon));

    MenuItem itemA = new MenuItem("Complaints", createIcon(FontAwesomeIcon.BOMB));
    MenuItem itemB = new MenuItem("Printing", createIcon(FontAwesomeIcon.PRINT));
    MenuItem itemC = new MenuItem("Settings", createIcon(FontAwesomeIcon.COGS));

    MenuItem showOverlay = new MenuItem("Show overlay");
    MenuItem showModalOverlay = new MenuItem("Show modal overlay");

    item21.getItems().addAll(item211, item212, item213, item214, item215);

    menu1.getItems().addAll(item11, item12, item13, item14);
    menu2.getItems().addAll(item21, item22);
    menu3.getItems().addAll(item31, item32, item33);

    // WorkbenchFX
    workbenchFx = WorkbenchFx.builder(
        new WidgetsTestModule(),
        new DropdownTestModule(),
        new CalendarModule(),
        new NotesModule(),
        new PreferencesModule(),
        new NavigationDrawerTestModule()
    )
        .toolbarLeft(
            new Button("Settings", new FontAwesomeIconView(FontAwesomeIcon.GEARS))
        )
        .toolbarRight(
            Dropdown.of(
                new FontAwesomeIconView(FontAwesomeIcon.ADDRESS_BOOK),
                new CustomMenuItem(new Label("Content 1")),
                new CustomMenuItem(new Label("Content 2"))
            ),
            Dropdown.of(
                new ImageView(CustomDemo.class.getResource("user_light.png").toExternalForm()),
                new Menu(
                    "Submenus", new FontAwesomeIconView(FontAwesomeIcon.PLUS),
                    new MenuItem("Submenu 1"),
                    new CustomMenuItem(new Label("CustomMenuItem"), false)
                )
            ),
            Dropdown.of(
                "Text",
                new ImageView(CustomDemo.class.getResource("user_light.png").toExternalForm()),
                new CustomMenuItem(new Label("Content 1")),
                new CustomMenuItem(new Label("Content 2"))
            )
        )
        .modulesPerPage(2)
        .tabFactory(tabFactory)
        .tileFactory(tileFactory)
        .pageFactory(pageFactory)
        .navigationDrawerFactory(navigationDrawerFactory)
        .navigationDrawer(menu1, menu2, menu3, itemA, itemB, itemC, showOverlay, showModalOverlay)
        .overlays(
            workbench -> new CustomOverlay(workbench, false),
            workbench -> new CustomOverlay(workbench, true)
        )
        .build();

    ObservableList<Node> overlays = workbenchFx.getOverlays();
    showOverlay.setOnAction(event -> workbenchFx.showOverlay(overlays.get(1), false));
    showModalOverlay.setOnAction(event -> workbenchFx.showOverlay(overlays.get(2), true));

//    workbenchFx.getStylesheets().add(CustomDemo.class.getResource("customTheme.css").toExternalForm());

    return workbenchFx;
  }

  private Node createIcon(FontAwesomeIcon icon) {
    FontAwesomeIconView fontAwesomeIconView = new FontAwesomeIconView(icon);
    fontAwesomeIconView.getStyleClass().add("icon");
    return fontAwesomeIconView;
  }
}
