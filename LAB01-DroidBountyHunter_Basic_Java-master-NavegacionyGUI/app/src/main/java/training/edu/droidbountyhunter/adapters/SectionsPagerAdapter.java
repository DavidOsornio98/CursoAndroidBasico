package training.edu.droidbountyhunter.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import training.edu.droidbountyhunter.R;
import training.edu.droidbountyhunter.fragments.AboutFragment;
import training.edu.droidbountyhunter.fragments.ListFragment;

@SuppressWarnings("deprecation")
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Fragment[] fragments;

/*    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }*/

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        // Se indica que se utilizaran 3 fragmentos para los tabs
        fragments = new Fragment[3];
    }

/*    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position + 1);
    }*/

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null){
            if (position < 2){
                fragments[position] = new ListFragment();
                Bundle arguments = new Bundle();
                arguments.putInt(ListFragment.ARG_SECTION_NUMBER,position);
                fragments[position].setArguments(arguments);
            }else {
                fragments[position] = new AboutFragment();
            }
        }
        return fragments[position];
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.title_fugitivos).toUpperCase();
            case 1:
                return context.getString(R.string.title_capturados).toUpperCase();
            case 2:
                return context.getString(R.string.title_acercade).toUpperCase();
            default:
                return null;
        }
    }


}