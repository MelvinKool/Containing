package org.nhl.containing;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import java.util.Date;
import org.lwjgl.opengl.Display;

public class HUD {
    
    private BitmapText dateText;
    private BitmapFont guiFont;

    
    public HUD(Node guiNode , BitmapFont guiFont) {
        this.guiFont = guiFont;
        initText(guiNode);
    }
    
    /**
     * Prepares the hud to write some text on the screen
     * @param guiNode The GUI Node of the application is needed.
     */
    private void initText(Node guiNode) {
        guiNode.detachAllChildren();
 
        dateText = new BitmapText(guiFont, false);
        dateText.setSize(guiFont.getCharSet().getRenderedSize());
        dateText.setText("gjhaeoughaeruogheuoghaeui");
        
        final float DATE_X = Display.getWidth() / 2 - dateText.getLineWidth() / 2;
        final float DATE_Y = Display.getHeight() - 10;
        
        dateText.setLocalTranslation(DATE_X, DATE_Y, 0);
        dateText.setName("DateTXT");
        guiNode.attachChild(dateText);
    }
    
    /**
     * Updates the Date text with the given date
     * @param currentDate the current date 
     */
    public void updateDateText(Date currentDate)
    {
        dateText.setText(currentDate.toString());
    }
}
