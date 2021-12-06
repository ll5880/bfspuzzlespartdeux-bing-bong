/**
 * Module description for the puzzles.
 * Required due to use of JavaFX.
 * YOU MAY RENAME THE MODULE.
 * @author RIT CS
 * November 2021
 */
module BFSPuzzles {
    requires transitive javafx.controls;
    requires java.logging;
    requires java.desktop;
    exports puzzles.tipover.gui;
    exports puzzles.lunarlanding.gui;
}
