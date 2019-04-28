package homelet.display;

import homelet.GH.visual.JCanvas;
import homelet.GH.visual.RenderManager;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame{
	
	private JCanvas canvas;
	
	public Display(String title, int width, int height) throws HeadlessException{
		super(title);
		setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		canvas = new JCanvas("Canvas");
		Dimension d = new Dimension(width, height);
		canvas.setPreferredSize(d);
		canvas.setMaximumSize(d);
		canvas.setPreferredSize(d);
		this.getContentPane().add(canvas);
		pack();
	}
	
	public JCanvas getCanvas(){
		return canvas;
	}
	
	public RenderManager getRenderManager(){
		return canvas.getCanvasThread().getRenderManager();
	}
}
