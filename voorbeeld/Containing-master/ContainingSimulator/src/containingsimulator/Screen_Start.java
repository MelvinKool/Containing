/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.dropdown.DropDownControl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Class of menu/pause screen 
 * @author Len
 */
public class Screen_Start extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    Screen screen;
    Main main;
    SimpleApplication app;
    private boolean escPressed = false;
    private float count = 0;
    private float max = 1;

    /**
     *Constructor
     * @param main
     */
    public Screen_Start(Main main) {
        this.main = main;
    }

    /**
     *Initializes app, add keymapping
     * @param stateManager
     * @param app
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        app.getInputManager().addMapping("back", new KeyTrigger(KeyInput.KEY_ESCAPE));
        app.getInputManager().addListener(actionListener, "back");

    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {

            if (name.equals("back")) {
                escPressed = true;

            }
        }
    };

    @Override
    public void update(float tpf) {
        /**
         * jME update loop!
         */
        if (escPressed) {
            if (count >= max) {
                count = 0;
                escPressed = false;
                startGame();
            } else {
                count += tpf * 10;
            }
        }
    }

    /**
     * Method to be called when switching from menu to ingame
     */
    public void startGame() {

        String connection = nifty.getScreen("start").findNiftyControl("ass", TextField.class).getRealText();
        String ip = connection;
        String myPort = nifty.getScreen("start").findNiftyControl("portField", TextField.class).getDisplayedText();
        DropDownControl dropDown1 = nifty.getScreen("start").findControl("dropDownRes", DropDownControl.class);
        String[] res = dropDown1.getSelection().toString().split("x");
        DropDownControl dropDownQuality = nifty.getScreen("start").findControl("dropDownQuality", DropDownControl.class);
        int gfxQuality = dropDownQuality.getSelectedIndex();
        boolean vSync = screen.findNiftyControl("checkBoxVsync", CheckBox.class).isChecked();
        int bpp = Integer.parseInt(nifty.getScreen("start").findControl("dropDownBPP", DropDownControl.class).getSelection().toString());
        boolean showFPS = screen.findNiftyControl("checkBoxFPS", CheckBox.class).isChecked();
        boolean showStats = screen.findNiftyControl("checkBoxStats", CheckBox.class).isChecked();
        try {
            int port = Integer.parseInt(myPort);
            int width = Integer.parseInt(res[0]);
            int height = Integer.parseInt(res[1]);
            main.startGame(ip, port, width, height, bpp, vSync, showFPS, showStats,gfxQuality);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
    }
    public void onEndScreen() {
    }

    /**
     *Stops the application
     */
    public void quitGame() {
        System.exit(1);
    }
}
