package np.com.aanalbasaula.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A Factory class to generate view for section adapter
 * Created by Aanal on 6/13/2016.
 */
public abstract class SectionViewFactory<T, D> {

    protected static int VIEW_TYPE_SECTION = 0;

    /**
     * Layout inflater for later view
     */
    protected LayoutInflater mInflater;

    SectionViewFactory(Context context){
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Get the number of views being used including the section view
     * @return the number of views being used including the section view
     */
    public abstract int getViewCount();

    /**
     * Get the view type of an item in the list.
     * @param section the section number, begins from 0
     * @param position absolute position of item in the list
     * @return the type of view
     */
    public abstract int getViewType(int section, int position);

    /**
     * Acquire a view of the specific type, section or any other view number defined by this factory
     * @param viewType the integer number associated to a view type
     * @return the view to be displayed
     */
    public abstract View getView(int viewType, ViewGroup parentView);

    /**
     * Populate the view using the provided data
     * @param viewType the type of view this is
     * @param view the actual view to be populated
     * @param data the data to be used while populating
     */
    public abstract void populateView(int viewType, View view, Object data);
}
