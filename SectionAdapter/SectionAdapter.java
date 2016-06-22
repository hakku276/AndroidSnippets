package np.com.aanalbasaula.shopsmart.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * A sectioned Adapter to handle sectioning of data
 * Created by Aanal on 6/13/2016.
 */
public class SectionAdapter<T, D> extends BaseAdapter {

    public enum DisplayOrder{
        normal, reverse
    }

    private static final String TAG = SectionAdapter.class.getSimpleName();

    protected Map<D, List<T>> dataTree;
    private SectionViewFactory<T, D> viewFactory;
    private DisplayOrder order;

    public SectionAdapter(SectionViewFactory<T, D> viewFactory) {
        if (viewFactory == null) {
            throw new RuntimeException("View Factory Cannot be null");
        }
        this.viewFactory = viewFactory;
        dataTree = new LinkedHashMap<>();
        order = DisplayOrder.normal;
    }

    public SectionAdapter(SectionViewFactory<T, D> viewFactory, DisplayOrder order) {
        if (viewFactory == null) {
            throw new RuntimeException("View Factory Cannot be null");
        }
        this.viewFactory = viewFactory;
        dataTree = new LinkedHashMap<>();
        this.order = order;
    }

    @Override
    public int getCount() {
        int count = dataTree.size();
        for (D key:
                dataTree.keySet()) {
            count += dataTree.get(key).size();
        }
        Log.d(TAG, "getCount: " + count);
        return count;
    }

    @Override
    public Object getItem(int position) {
        if(order == DisplayOrder.normal) {
            for (D key :
                    dataTree.keySet()) {
                if (position == 0) {
                    return key;
                }
                //reduce the position for this section display
                position--;
                if (position < dataTree.get(key).size()) {
                    //the item is in this section
                    return dataTree.get(key).get(position);
                }
                position = position - dataTree.get(key).size();
            }
        } else {
            LinkedList<D> keyList = new LinkedList<>(dataTree.keySet());
            ListIterator<D> iterator = keyList.listIterator(keyList.size());
            while(iterator.hasPrevious()){
                D key = iterator.previous();
                if(position == 0){
                    return key;
                }
                //reduce the position for this section display
                position--;
                if (position < dataTree.get(key).size()) {
                    //the item is in this section
                    List<T> dataList = dataTree.get(key);
                    //get the list data in reverse order
                    return dataList.get(dataList.size()-1-position);
                }
                position = position - dataTree.get(key).size();
            }
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return viewFactory.getViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + position);
        int section = 0;
        if(order == DisplayOrder.normal) {
            Log.d(TAG, "getItemViewType: Normal Display Order");
            for (D key :
                    dataTree.keySet()) {
                if (position == 0) {
                    //position 0 means that the item is a section header
                    return 0;
                }
                //reduce the position for this section display
                position--;
                if (position < dataTree.get(key).size()) {
                    //position lies within the item list for this section
                    return viewFactory.getViewType(section, position);
                }
                //reduce the position for the items under the section
                position = position - dataTree.get(key).size();
                //moving on to another section
                section++;
            }
        }else {
            Log.d(TAG, "getItemViewType: Reverse Display Order (keySetSize - " + dataTree.size()+")");
            LinkedList<D> keyList = new LinkedList<>(dataTree.keySet());
            ListIterator<D> iterator = keyList.listIterator(keyList.size());
            while(iterator.hasPrevious()){
                D key = iterator.previous();
                if(position == 0){
                    return 0;
                }
                //reduce the position for this section display
                position--;
                if (position < dataTree.get(key).size()) {
                    //the item is in this section
                    List<T> dataList = dataTree.get(key);
                    //position lies within the item list for this section
                    return viewFactory.getViewType(section, dataList.size()-1-position);
                }
                position = position - dataTree.get(key).size();
                //moving on to another section
                section ++;
            }
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        Log.d(TAG, "getView: position- " + position + " type- " + viewType + " convertView- " + convertView);
        if(convertView == null){
            //get the view to be displayed
            convertView = viewFactory.getView(viewType, parent);
        }
        //populate the view
        viewFactory.populateView(viewType, convertView, getItem(position));
        //return the view
        return convertView;
    }

    public void addItem(D section, T item){
        if(dataTree.containsKey(section)) {
            dataTree.get(section).add(item);
        } else {
            addSection(section);
            addItem(section, item);
        }
    }

    public void addSection(D section){
        if(!dataTree.containsKey(section)) {
            dataTree.put(section, new ArrayList<T>());
        }
    }

    public List<T> getSection(D section){
        return dataTree.get(section);
    }
}
