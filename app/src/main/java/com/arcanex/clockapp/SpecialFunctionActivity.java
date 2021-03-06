package com.arcanex.clockapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;
import static com.arcanex.clockapp.MainActivity.homeLocation;
import static com.arcanex.clockapp.MainActivity.service;


public class SpecialFunctionActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_spectial_function);
        ViewPager pager = findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);


    }


    public static class WeatherTrackFragment extends Fragment {

        WeatherTrackFragment newInstance() {
            WeatherTrackFragment weatherTrackFragment = new WeatherTrackFragment();

            return weatherTrackFragment;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getView().findViewById(R.id.rain_track_info).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "????????????", Toast.LENGTH_SHORT).show();


                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setPositiveButton("????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("???????????????????? ?? Rain Track")
                            .setMessage("  ???????? ???? ?????????? ???????????? ??????????, ???? ???????????? ???????????? ?????????????????? ?????????????????? ?????????? ???????????? (?????????? ?????????? ??????????). ???? ?????????? ???????????? ?????????????????? ??????????????, ?????????? ???????????????????????? ???????? ?? ?????????????????????????? ????????????????, ?? ?????????????????????? ???? ????????, ???????????????? ???? ???? ?????????? ??????????????????????. ?????????? ?????????????? ?????????? ???????????????? ????????????????????????, ?????????? ???????????? ?????? ???????????? ?????????????????? ?? ?????????????? ????????????.")
                            .create();
                    alertDialog.show();
                    TextView textView = alertDialog.findViewById(android.R.id.message);
                    textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.nokia_light));

                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.raintrack_function, null);
            getChildFragmentManager().beginTransaction().add(R.id.pref, new WeatherTrackPreferencesFragment()).commit();
            return view;

        }

    }

    public static class WeatherTrackPreferencesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            RecyclerView recyclerView = getListView();
            DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(), VERTICAL);
            recyclerView.addItemDecoration(itemDecor);


        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.raintrack_function_fragment, rootKey);
            getPreferenceManager().getPreferenceScreen().getPreference(0).setIconSpaceReserved(false);
            getPreferenceManager().getPreferenceScreen().getPreference(0).setEnabled(false);
            getPreferenceManager().getPreferenceScreen().getPreference(1).setIconSpaceReserved(false);
            getPreferenceManager().getPreferenceScreen().getPreference(2).setIconSpaceReserved(false);
            getPreferenceManager().getPreferenceScreen().getPreference(2).setEnabled(false);


        }
    }

    public static class HomeTrackFragment extends Fragment {

        HomeTrackFragment newInstance() {
            HomeTrackFragment homeTrackFragment = new HomeTrackFragment();

            return homeTrackFragment;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getView().findViewById(R.id.home_track_info).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setPositiveButton("????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setTitle("???????????????????? ?? Home Track")
                            .setMessage("???????? ???????????????? ?????????????? ?????????????? ???? ????????, ???? ???????????? ?????????????????????????? ?????????????????????? ?????? ???????????????? ?????????????? ?????????????????????? ?? ???????????????????????????????? ??????????. (???? ???????????? ???? ???????????? ??????????????. ???????????? ?????????????????????? ????????????????????, ?????????????????????????? ?????????? ???????????????? ?????? ???? ????????. (???????? ?????????? ???????????????? ???? ??????????????????.)")
                            .create();
                    alertDialog.show();
                    TextView textView = alertDialog.findViewById(android.R.id.message);
                    textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.nokia_light));
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.hometrack_function, null);
            getChildFragmentManager().beginTransaction().add(R.id.pref, new HomeTrackPreferencesFragment()).commit();
            return view;

        }

    }

    public static class HomeTrackPreferencesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            RecyclerView recyclerView = getListView();
            DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(), VERTICAL);
            recyclerView.addItemDecoration(itemDecor);


        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.hometrack_function_fragment, rootKey);
            getPreferenceManager().getPreferenceScreen().getPreference(0).setIconSpaceReserved(false);
            getPreferenceManager().getPreferenceScreen().getPreference(0).setEnabled(false);
            getPreferenceManager().getPreferenceScreen().getPreference(0).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                        return false;
                    } else {
                        return true;
                    }

                }
            });
            getPreferenceManager().getPreferenceScreen().getPreference(1).setIconSpaceReserved(false);
            getPreferenceManager().getPreferenceScreen().getPreference(1).setEnabled(true);
            getPreferenceManager().getPreferenceScreen().getPreference(1).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    getPreferenceManager().getSharedPreferences().edit().putString("homeAddress", service.locationHelper.setHomeAddress()).commit();
                    getPreferenceManager().getPreferenceScreen().getPreference(2).setSummary("?????????????? ??????????: " + getPreferenceManager().getSharedPreferences().getString("homeAddress", ""));




                    return false;

                }
            });
            getPreferenceManager().getPreferenceScreen().getPreference(2).setIconSpaceReserved(false);
            getPreferenceManager().getPreferenceScreen().getPreference(2).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String address = service.locationHelper.setHomeAddress((String) newValue);
                    preference.setSummary("?????????????? ??????????: " + address);
                    getPreferenceManager().getSharedPreferences().edit().putString(preference.getKey(), address).commit();
                    return false;
                }
            });
            getPreferenceManager().getPreferenceScreen().getPreference(2).setSummary("?????????????? ??????????: " + getPreferenceManager().getSharedPreferences().getString("homeAddress", ""));

        }
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new WeatherTrackFragment().newInstance();
            } else {
                return new HomeTrackFragment().newInstance();
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
