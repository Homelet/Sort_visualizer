package homelet.sort;

import homelet.GH.handlers.GH;
import homelet.GH.utils.IntervalCaller;
import homelet.GH.visual.interfaces.Renderable;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;

/**
 * the basic algorithm is as follow:<br>
 * when every time sort, choose the smallest value from the data, and put it on the first of the list, until all value <br>
 * has been sorted
 */
public class SelectionSortSimulator extends LinkedList<Double> implements Renderable{
	
	private int    progress = 0;
	private double maxBounds, minbounds;
	private IntervalCaller caller;
	private boolean ticking = true;
	private int currentWorkingIndex = 1;
	private int currentMinIndex     = 0;
	private Color finishedColor        = new Color(0xFF6600);
	private Color untouchedColor       = new Color(0x999999);
	private Color currentMinIndexColor = new Color(0x3300ff);
	private Color currentWorkingColor  = new Color(0x33FF00);
	
	public SelectionSortSimulator(Collection<Double> data, double minbounds, double maxBounds){
		super(data);
		this.maxBounds = maxBounds;
		this.minbounds = minbounds;
		this.caller = new IntervalCaller(this::sort, 0L);
	}
	
	@Override
	public void tick(){
		caller.tick();
	}
	
	@Override
	public void render(Graphics2D g){
		Rectangle bounds     = g.getClipBounds();
		double    unitHeight = bounds.getHeight() / (maxBounds - minbounds);
		double    unitWidth  = bounds.getWidth() / this.size();
		for(int index = 0; index < this.size(); index++){
			double value = this.get(index);
			Shape  shape = GH.rectangle(false, index * unitWidth, (maxBounds - value) * unitHeight, unitWidth, value * unitHeight);
			if(index < progress){
				g.setColor(finishedColor);
			}else{
				if(index == currentMinIndex){
					g.setColor(currentMinIndexColor);
				}else if(index == currentWorkingIndex){
					g.setColor(currentWorkingColor);
				}else{
					g.setColor(untouchedColor);
				}
			}
			g.fill(shape);
			g.setColor(Color.BLACK);
			g.draw(shape);
		}
	}
	
	@Override
	public boolean isTicking(){
		return ticking;
	}
	
	@Override
	public boolean isRendering(){
		return true;
	}

	private void sort(){
		doingRevolution();
	}

	private void preparingEachRevolution(){
		if(progress + 1 == this.size()){
			ticking = false;
			this.currentWorkingIndex = -1;
			this.currentMinIndex = -1;
			progress++;
			return;
		}
		this.currentWorkingIndex = progress + 1;
		this.currentMinIndex = progress;
		doingRevolution();
	}

	private void doingRevolution(){
		if(this.currentWorkingIndex == this.size()){
			finishingEachRevolution();
			return;
		}
		int result = this.get(currentWorkingIndex).compareTo(this.get(currentMinIndex));
		if(result <= 0){
			currentMinIndex = currentWorkingIndex;
		}
		currentWorkingIndex++;
	}
	
	private void finishingEachRevolution(){
		if(currentMinIndex != progress)
			this.add(progress, this.remove(currentMinIndex));
		progress++;
		preparingEachRevolution();
	}
}
