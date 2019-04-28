package homelet.display;

import java.util.Arrays;
import java.util.Collection;

public class Sort{
	
	private Sort(){}
	
	public static <E extends Comparable<E>> void sort(Collection<E> data, E[] storeDataArray, SortMode sortMode){
		sort(data.toArray(storeDataArray), sortMode);
	}
	
	public static <E extends Comparable<E>> void sort(E[] data, SortMode sortMode){
		switch(sortMode){
			case MERGE_SORT:
				merge_sort(data);
				break;
			case INSERTION_SORT:
				insertion_sort(data);
				break;
			case SELECTION_SORT:
				selection_sort(data);
				break;
			case SINGLE_PIVOT_QUICK_SORT:
				single_pivot_quick_sort(data);
				break;
			case DUAL_PIVOT_QUICK_SORT:
				dual_pivot_quick_sort(data);
				break;
			case TRIPLE_BUFFERED_QUICK_SORT:
				triple_Buffered_quick_sort(data);
				break;
			default:
				break;
		}
	}
	
	private static <E extends Comparable<E>> void merge_sort(E[] data){
		merge_sort_split(data, 0, data.length - 1);
	}
	
	/**
	 * divide all data from leftBounds to rightBounds in half
	 *
	 * @param data        the data
	 * @param leftBounds  left bounds
	 * @param rightBounds right bounds
	 */
	private static <E extends Comparable<E>> void merge_sort_split(E[] data, int leftBounds, int rightBounds){
		// if left bounds is equals or bigger than right bounds means the left and right collapsed(no value left between left and right)
		if(leftBounds >= rightBounds)
			return;
		// calculate the middle
		int middle = (leftBounds + rightBounds) / 2;
		// continue to split the to left part and right part
		merge_sort_split(data, leftBounds, middle);
		merge_sort_split(data, middle + 1, rightBounds);
		// if spitted, starts merging
		merge_sort_merge(data, leftBounds, middle, rightBounds);
	}
	
	/**
	 * merge [left...mid] and [mid+1...right]
	 *
	 * @param data        the data set
	 * @param leftBounds  the left bounds
	 * @param mid         the middle point
	 * @param rightBounds the right bounds
	 */
	private static <E extends Comparable<E>> void merge_sort_merge(E[] data, int leftBounds, int mid, int rightBounds){
		E[] subData = Arrays.copyOfRange(data, leftBounds, rightBounds + 1);
		// init, left_pointer points to left start, right_pointer points right start
		int left_pointer  = leftBounds;
		int right_pointer = mid + 1;
		// iterate through the whole collection
		for(int index = leftBounds; index <= rightBounds; index++){
			if(left_pointer > mid){                                                                           // if all left has been processed
				data[index] = subData[right_pointer - leftBounds];
				right_pointer++;
			}else if(right_pointer > rightBounds){                                                            // if all right has been processed
				data[index] = subData[left_pointer - leftBounds];
				left_pointer++;
			}else if(subData[left_pointer - leftBounds].compareTo(subData[right_pointer - leftBounds]) < 0){  // if left data < right data then let left data add
				data[index] = subData[left_pointer - leftBounds];
				left_pointer++;
			}else{                                                                                            // if left data >= right data then let right data add
				data[index] = subData[right_pointer - leftBounds];
				right_pointer++;
			}
		}
	}
	
	private static <E extends Comparable<E>> void single_pivot_quick_sort(E[] data){
		single_pivot_quick_sort_partition(data, 0, data.length - 1);
	}
	
	private static <E extends Comparable<E>> void single_pivot_quick_sort_partition(E[] data, int leftBounds, int rightBounds){
		// if value is == 0, means no data
		if(leftBounds >= rightBounds)
			return;
		// the boundaries that divide the bigger array and the smaller array
		int pivot = leftBounds;
		// starts iteration, from index 1
		for(int index = leftBounds + 1; index <= rightBounds; index++){
			// if bigger or equals to don't do anything, because it is already in the desired positions
			// if smaller then swap the start of greater with the current
			if(data[index].compareTo(data[leftBounds]) < 0){
				pivot++;
				swap(data, pivot, index);
			}
		}
		// swap the flag value(first value) with the end of smaller
		swap(data, pivot, leftBounds);
		// call partition again on the remaining (leftBounds ~ (boundaries - 1)) & (boundaries ~ rightBounds)
		single_pivot_quick_sort_partition(data, leftBounds, pivot - 1);
		single_pivot_quick_sort_partition(data, pivot + 1, rightBounds);
	}
	
	private static <E extends Comparable<E>> void dual_pivot_quick_sort(E[] data){
		dual_pivot_quick_sort_partition(data, 0, data.length - 1);
	}
	
	private static <E extends Comparable<E>> void dual_pivot_quick_sort_partition(E[] data, int leftBounds, int rightBounds){
		// if value between leftBounds and rightBounds smaller than 0 stop processing
		if(leftBounds >= rightBounds)
			return;
		// init pivot_left points to leftBounds + 1, pivot_right points to rightBounds
		int pivot_left  = leftBounds + 1;
		int pivot_right = rightBounds;
		// iterate until find
		while(true){
			// finding the next value which is bigger or equals to the flag value
			while(pivot_left <= rightBounds && data[pivot_left].compareTo(data[leftBounds]) < 0)
				pivot_left++;
			// finding the next value which is smaller or equals to the flag value
			while(pivot_right >= leftBounds + 1 && data[pivot_right].compareTo(data[leftBounds]) > 0)
				pivot_right--;
			// if all value has been iterated, then break
			if(pivot_left >= pivot_right)
				break;
			// if so, swap the left and right
			// to let pivot_left merge to the part which is greater
			// and pivot_right merge to the part which is smaller
			// if equals then mearged to the center
			swap(data, pivot_left, pivot_right);
			// move to next value
			pivot_left++;
			pivot_right--;
		}
		// swap the flag with the end of the equals part
		swap(data, leftBounds, pivot_right);
		// call partition again on the greater part, and the smaller part
		dual_pivot_quick_sort_partition(data, leftBounds, pivot_left - 1);
		dual_pivot_quick_sort_partition(data, pivot_right + 1, rightBounds);
	}
	
	private static <E extends Comparable<E>> void triple_Buffered_quick_sort(E[] data){
		triple_Buffered_quick_sort_partition(data, 0, data.length - 1);
	}
	
	private static <E extends Comparable<E>> void triple_Buffered_quick_sort_partition(E[] data, int leftBounds, int rightBounds){
		if(leftBounds >= rightBounds)
			return;
		int lessThen    = leftBounds;
		int greaterThen = rightBounds + 1;
		for(int index = leftBounds + 1; index < greaterThen; ){
			int result = data[index].compareTo(data[leftBounds]);
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
		// swap the flag with the end of the equals part
		swap(data, leftBounds, lessThen);
		// call partition again on the greater part and the smaller part
		triple_Buffered_quick_sort_partition(data, leftBounds, lessThen - 1);
		triple_Buffered_quick_sort_partition(data, greaterThen, rightBounds);
	}
	
	private static <E extends Comparable<E>> void insertion_sort(E[] data){
		// start sorting, iterate througe the whole collection, starts from index 1
		for(int index = 1; index < data.length; index++){
			E value = data[index];
			// find the desired index for current value
			int desiredIndex = index;
			for(int inner_index = index - 1; inner_index >= 0; inner_index--){
				if(value.compareTo(data[inner_index]) < 0){
					// if smaller means needs continue to check
					desiredIndex = inner_index;
				}else{
					// if bigger or equals to means has found
					break;
				}
			}
			// move it to the desired index
			if(desiredIndex != index){
				System.arraycopy(data, desiredIndex, Arrays.copyOfRange(data, desiredIndex, index), desiredIndex + 1, desiredIndex - index);
				data[desiredIndex] = value;
			}
		}
	}
	
	private static <E extends Comparable<E>> void selection_sort(E[] data){
		// start sorting, iterate through the whole collection
		for(int index = 0; index < data.length; index++){
			// starts to find the local min, and move it to index
			int localMinIndex = index;
			for(int inner_index = index + 1; inner_index < data.length; inner_index++){
				// if smaller than localMin, then set the localMin to be current inner_index
				if(data[inner_index].compareTo(data[localMinIndex]) < 0){
					localMinIndex = inner_index;
				}
			}
			// move localMin to index
			if(localMinIndex != index){
				E value = data[localMinIndex];
				System.arraycopy(data, index, Arrays.copyOfRange(data, index, localMinIndex), index + 1, index - localMinIndex);
				data[index] = value;
			}
		}
	}
	
	public static <E extends Comparable<E>> void swap(E[] data, int index_1, int index_2){
		if(index_1 == index_2)
			return;
		E e = data[index_1];
		data[index_1] = data[index_2];
		data[index_2] = e;
	}
	
	public enum SortMode{
		INSERTION_SORT,
		SELECTION_SORT,
		MERGE_SORT,
		SINGLE_PIVOT_QUICK_SORT,
		DUAL_PIVOT_QUICK_SORT,
		TRIPLE_BUFFERED_QUICK_SORT;
	}
}
