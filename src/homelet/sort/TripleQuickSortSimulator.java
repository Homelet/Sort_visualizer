package homelet.sort;

import homelet.GH.handlers.GH;
import homelet.GH.utils.IntervalCaller;
import homelet.GH.visual.interfaces.Renderable;
import homelet.display.Utility;

import java.awt.*;
import java.util.LinkedList;

import static homelet.display.Sort.swap;

public class TripleQuickSortSimulator implements Renderable{
	
	double minBounds, maxBounds;
	Double[]       data;
	IntervalCaller caller;
	
	public TripleQuickSortSimulator(Double[] data, double minBounds, double maxBounds){
		this.minBounds = minBounds;
		this.maxBounds = maxBounds;
		this.data = data;
		greaterThen = data.length;
		currentRightBounds = data.length - 1;
		caller = new IntervalCaller(this::doingRevolution, 0L);
	}
	
	int lessThen = 0, greaterThen, currentRightBounds, currentLeftBounds = 0, index = 1;
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
		this.lessThen = currentLeftBounds;
		this.greaterThen = currentRightBounds + 1;
		this.index = currentLeftBounds + 1;
		doingRevolution();
	}
	
	private void doingRevolution(){
		if(index >= greaterThen){
			finishingRevolution();
			return;
		}
		int result = data[index].compareTo(data[currentLeftBounds]);
		if(result < 0){
			// if smaller than flag, swap with the first of the equal bounds (end of the lessThen)
			lessThen++;
			swap(data, index, lessThen);
			index++;
		}else if(result > 0){
			// if bigger than flag, swap with the first of greaterThen
			greaterThen--;
			swap(data, index, greaterThen);
			// no index++ because the swapped value is unhandled value, needs to handle it in the next loop
		}else{
			// if equals, don't swap and merge it with the end of equal
			index++;
		}
	}
	
	private void finishingRevolution(){
		swap(data, currentLeftBounds, lessThen);
		if(currentLeftBounds < currentRightBounds)
			boundsList.add(new int[][]{ { currentLeftBounds, lessThen - 1 }, { greaterThen, currentRightBounds } });
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
	private Color indexColor             = new Color(0xffff00);
	
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
					if(index == this.index){
						g.setColor(indexColor);
					}else if(index == currentLeftBounds || index == currentRightBounds){
						g.setColor(left_right_Color);
					}else if(index == lessThen || index == greaterThen){
						g.setColor(left_right_pivot_Color);
					}else if(index < lessThen){
						g.setColor(leftColor);
					}else if(index > greaterThen){
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
