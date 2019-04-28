package homelet.sort;

import homelet.GH.handlers.GH;
import homelet.GH.utils.IntervalCaller;
import homelet.GH.visual.interfaces.Renderable;
import homelet.display.Sort;
import homelet.display.Utility;

import java.awt.*;
import java.util.LinkedList;

public class DoubleQuickSortSimulator implements Renderable{
	
	double minBounds, maxBounds;
	Double[]       data;
	IntervalCaller caller;
	
	public DoubleQuickSortSimulator(Double[] data, double minBounds, double maxBounds){
		this.minBounds = minBounds;
		this.maxBounds = maxBounds;
		this.data = data;
		pivot_right = data.length - 1;
		currentRightBounds = data.length - 1;
		caller = new IntervalCaller(this::doingRevolution, 0L);
	}
	
	int pivot_left = 1, pivot_right, currentRightBounds = 0, currentLeftBounds;
	LinkedList<int[][]> boundsList = new LinkedList<>();
	int[]               lastLeft   = null;
	
	private void preparingRevolution(){
		int newLeftBounds;
		int newRightBounds;
		if(lastLeft == null){
			int[][] bounds = boundsList.poll();
			if(bounds == null){
				finished = true;
				System.out.println("Result : " + Utility.checkSorted(data));
				return;
			}
			newLeftBounds = bounds[0][0];
			newRightBounds = bounds[0][1];
			lastLeft = bounds[1];
		}else{
			newLeftBounds = lastLeft[0];
			newRightBounds = lastLeft[1];
			lastLeft = null;
		}
		this.currentLeftBounds = newLeftBounds;
		this.currentRightBounds = newRightBounds;
		this.pivot_left = currentLeftBounds + 1;
		this.pivot_right = currentRightBounds;
		doingRevolution();
	}
	
	private void doingRevolution(){
		while(pivot_left < data.length && pivot_left <= currentRightBounds && data[pivot_left].compareTo(data[currentLeftBounds]) < 0)
			pivot_left++;
		while(pivot_right >= currentLeftBounds + 1 && data[pivot_right].compareTo(data[currentLeftBounds]) > 0)
			pivot_right--;
		if(pivot_left >= pivot_right){
			finishingRevolution();
			return;
		}
		Sort.swap(data, pivot_left, pivot_right);
		pivot_left++;
		pivot_right--;
	}
	
	private void finishingRevolution(){
		Sort.swap(data, currentLeftBounds, pivot_right);
		if(currentLeftBounds < currentRightBounds)
			boundsList.add(new int[][]{ { currentLeftBounds, pivot_left - 1 }, { pivot_right + 1, currentRightBounds } });
		preparingRevolution();
	}
	
	@Override
	public void tick(){
		caller.tick();
	}
	
	private Color left_right_Color       = new Color(0x3300ff);
	private Color left_right_pivot_Color = new Color(0x33FF00);
	private Color leftColor              = new Color(0xFFFF);
	private Color middleColor            = new Color(0xFF6600);
	private Color rightColor             = new Color(0xff00ff);
	private Color otherColor             = new Color(0x999999);
	
	@Override
	public void render(Graphics2D g){
		Rectangle bounds     = g.getClipBounds();
		double    unitHeight = bounds.getHeight() / (maxBounds - minBounds);
		double    unitWidth  = bounds.getWidth() / data.length;
		for(int index = 0; index < data.length; index++){
			double value = data[index];
			Shape  shape = GH.rectangle(false, index * unitWidth, (maxBounds - value) * unitHeight, unitWidth, value * unitHeight);
			if(!finished){
				if(index >= currentLeftBounds && index <= currentRightBounds){
					if(index == currentLeftBounds || index == currentRightBounds){
						g.setColor(left_right_Color);
					}else if(index == pivot_left || index == pivot_right){
						g.setColor(left_right_pivot_Color);
					}else if(index < pivot_left){
						g.setColor(leftColor);
					}else if(index > pivot_right){
						g.setColor(rightColor);
					}else{
						g.setColor(middleColor);
					}
				}else{
					g.setColor(otherColor);
				}
			}else{
				g.setColor(middleColor);
			}
			g.fill(shape);
			g.setColor(Color.BLACK);
			g.draw(shape);
		}
	}
	
	boolean finished = false;
	
	@Override
	public boolean isTicking(){
		return !finished;
	}
}
