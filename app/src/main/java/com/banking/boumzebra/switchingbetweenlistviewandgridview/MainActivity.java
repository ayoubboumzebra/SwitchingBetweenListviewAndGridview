package com.banking.boumzebra.switchingbetweenlistviewandgridview;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import model.Article;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ViewStub stubGrid;
    public ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<Article> articleList;
    public int currentViewMode = 0;
    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;
    private Menu menu;
    private String keyword;
    private ArrayList<Article> search_result_arraylist;
    private MenuItem mSearchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        stubGrid = (ViewStub) findViewById(R.id.stub_grid);
        stubList = (ViewStub) findViewById(R.id.stub_list);

        stubGrid.inflate();
        stubList.inflate();

        listView = (ListView) findViewById(R.id.mylistview);
        gridView = (GridView) findViewById(R.id.mygridview);

        //Get list for articles
        getArticleList();

        //Get current view mode in share reference
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", Context.MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currenteViewMode", VIEW_MODE_LISTVIEW);

        switchView();

    }

    public void switchView() {
        if (VIEW_MODE_LISTVIEW == currentViewMode) {
            stubList.setVisibility(View.VISIBLE);
            stubGrid.setVisibility(View.GONE);
        } else {
            stubList.setVisibility(View.GONE);
            stubGrid.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }

    public void setAdapters() {

        if (VIEW_MODE_LISTVIEW == currentViewMode) {
            listViewAdapter = new ListViewAdapter(this, R.layout.list_item, articleList);
            listViewAdapter.notifyDataSetChanged();
            listView.setAdapter(listViewAdapter);
        } else {
            gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, articleList);
            gridViewAdapter.notifyDataSetChanged();
            gridView.setAdapter(gridViewAdapter);
        }

    }

    public List<Article> getArticleList() {
        articleList = new ArrayList<>();
        articleList.add(new Article(0, "Nature N 1", "Nature photography is a wide range of photography taken outdoors and devoted to displaying natural elements such as landscapes, wildlife, plants, and close-ups of natural scenes and textures", R.drawable.a));
        articleList.add(new Article(0, "Nature N 2", "Nature photography is a wide range of photography taken outdoors and devoted to displaying natural elements such as landscapes, wildlife, plants, and close-ups of natural scenes and textures", R.drawable.b));
        articleList.add(new Article(0, "Nature N 3", "Nature photography is a wide range of photography taken outdoors and devoted to displaying natural elements such as landscapes, wildlife, plants, and close-ups of natural scenes and textures", R.drawable.c));
        articleList.add(new Article(0, "Nature N 4", "Nature photography is a wide range of photography taken outdoors and devoted to displaying natural elements such as landscapes, wildlife, plants, and close-ups of natural scenes and textures", R.drawable.d));
        articleList.add(new Article(0, "Nature N 5", "Nature photography is a wide range of photography taken outdoors and devoted to displaying natural elements such as landscapes, wildlife, plants, and close-ups of natural scenes and textures", R.drawable.e));
        articleList.add(new Article(0, "Nature N 6", "Nature photography is a wide range of photography taken outdoors and devoted to displaying natural elements such as landscapes, wildlife, plants, and close-ups of natural scenes and textures", R.drawable.f));
        articleList.add(new Article(0, "Nature N 7", "Nature photography is a wide range of photography taken outdoors and devoted to displaying natural elements such as landscapes, wildlife, plants, and close-ups of natural scenes and textures", R.drawable.g));
        return articleList;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search_item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Search");
//        search_item.expandActionView();

        //search items
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (VIEW_MODE_LISTVIEW == currentViewMode) {
                    listViewAdapter.getFilter().filter(s);
                } else {
                    gridViewAdapter.getFilter().filter(s);
                }
                return false;
            }
        });

        //Hide action_list whene searchview is expanded
        search_item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                if (menu != null) {
                    menu.findItem(R.id.action_list).setVisible(false);
                }
                return true; // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (menu != null) {
                    menu.findItem(R.id.action_list).setVisible(true);
                }
                return true; // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_list:
                if (VIEW_MODE_LISTVIEW == currentViewMode) {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_view_list));
                    currentViewMode = VIEW_MODE_GRIDVIEW;
                } else {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_view_grid));
                    currentViewMode = VIEW_MODE_LISTVIEW;
                }

                switchView();
                SharedPreferences sharedPreferences = this.getSharedPreferences("ViewMode", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentViewMode", currentViewMode);
                editor.commit();
                    break;
                }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
