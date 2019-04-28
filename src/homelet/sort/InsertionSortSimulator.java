package homelet.sort;

import homelet.GH.handlers.GH;
import homelet.GH.utils.IntervalCaller;
import homelet.GH.visual.interfaces.Renderable;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;

/**
 * the basic Algorithm is sorts from the start of the list, and when each time the next value append, put it to a position in the start
 * until all list has been sorted<br>
 * 每一次都将下一个数值，排序到已经排好的队列的合适位置，直到全部排序完成
 */
public class InsertionSortSimulator extends LinkedList<Double> implements Renderable{
	
	//
	private double maxBounds, minbounds;
	private IntervalCaller caller;
	private boolean ticking = true;
	private int progress             = 0;
	private int currentCheckingIndex = 0;
	private int desiredIndex         = 0;
	private int lastDesiredindex     = 0;
	private Color finishedColor        = new Color(0xFF6600);
	private Color untouchedColor       = new Color(0x999999);
	private Color currentDesiredColor  = new Color(0x3300ff);
	private Color currentCheckingColor = new Color(0x33FF00);
	
	public InsertionSortSimulator(Collection<Double> doubles, double minBounds, double maxBounds){
		super(doubles);
		this.minbounds = minBounds;
		this.maxBounds = maxBounds;
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
				if(index == currentCheckingIndex){
					g.setColor(currentCheckingColor);
				}else if(index == lastDesiredindex){
					g.setColor(currentDesiredColor);
				}else{
					g.setColor(finishedColor);
				}
			}else{
				g.setColor(untouchedColor);
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

	private void preparingRevolution(){
		if(progress == this.size()){
			currentCheckingIndex = -1;
			desiredIndex = -1;
			lastDesiredindex = -1;
			progress++;
			this.ticking = false;
			return;
		}
		this.desiredIndex = progress;
		this.currentCheckingIndex = progress - 1;
	}

	private void doingRevolution(){
		if(currentCheckingIndex < 0){
			finishingRevolution();
			doingRevolution();
			return;
		}
		if(this.get(progress) < this.get(currentCheckingIndex)){
			// if smaller means needs continue to check
			desiredIndex = currentCheckingIndex;
		}else{
			// if bigger or equals means has found
			finishingRevolution();
			return;
		}
		currentCheckingIndex--;
	}
	
	private void finishingRevolution(){
		lastDesiredindex = desiredIndex;
		if(this.progress != this.desiredIndex)
			this.add(desiredIndex, this.remove(progress));
		progress++;
		preparingRevolution();
	}
}
