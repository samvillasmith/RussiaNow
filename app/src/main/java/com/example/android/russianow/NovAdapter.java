package com.example.android.russianow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class NovAdapter extends ArrayAdapter<Novosti> {

    /**
     * This is essential to separate the timestamp from the date of the story, as well
     * as from the title from any additional identifiers.
     */
    private static final String TIMESTAMP_DEMARCATION;

    static {
        TIMESTAMP_DEMARCATION = "T";
    }

    private static final String STORY_DEMARCATION;

    static {
        STORY_DEMARCATION = "|";
    }

    public String latestStory;

    /**
     * Constructs a new {@link NovAdapter}.
     *
     * @param context of the app as a super constructor
     *
     */

    public NovAdapter(Context context, ArrayList<Novosti> nov) {
        super(context, 0, nov);
    }

    /**
     * Returns a list item view that displays information about the stories at given positions.
     */


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        /*
        The purpose of these two blocks is to link the various sections of the xml layout to this java
        file by declaring various TextViews within a ViewHolder method. First, this is done by
        assigning TextViews to new unique objects
        */

        class ViewHolder {
            private TextView regionTextView;
            private TextView authorTextView;
            private TextView timeStampTextView;
            private TextView titleTextView;

            /*

            Next, it's important to assign the new TexView objects to the various xml views for
            the program to be able to reference.

            */


            private ViewHolder(View itemView) {
                regionTextView = itemView.findViewById(R.id.region);
                authorTextView = itemView.findViewById(R.id.author);
                timeStampTextView = itemView.findViewById(R.id.timestamp);
                titleTextView = itemView.findViewById(R.id.article_name);
            }
        }

        /*
        Check if there is an existing list item view (called convertView) that we can reuse,
        otherwise, if convertView is null, then inflate a new list item layout.
        I tried doing this a few other ways, but ConvertView is the best option for
        its ability to recycle what isn't used.
        Using ViewHolder is incredibly important for the efficiency of the app
        and I realize that by invoking a lot of R.id objects, it could slow things
        down, so it took me a while to figure this out, but ViewHolder is very efficient.
        At first, I didn't fully understand how this was related to recycling, but now I do. Nested
        if and else if statements were also very useful to me here to display demarcations.
        */

        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.nov_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if (position >= getCount()) {
            return convertView;
        }
        Novosti novostiRossiy;
        novostiRossiy = getItem(position);

        holder.regionTextView.setText(novostiRossiy.getmSection());

        String timeStamp;
        timeStamp = novostiRossiy.getmDateTime();
        if (!timeStamp.contains(TIMESTAMP_DEMARCATION)) {
        } else {
            String[] componentDivision;
            componentDivision = TIMESTAMP_DEMARCATION.split(timeStamp);
            latestStory = componentDivision[0];
        }
        holder.titleTextView.setText(latestStory);

        String authors;
        authors = novostiRossiy.getmAuthor();
        holder.authorTextView.setText(authors);
        String story;
        story = novostiRossiy.getmTitle();
        if (!story.contains(novostiRossiy.getmAuthor())) {
        } else {
            if (!story.contains(STORY_DEMARCATION)) {
            } else if (story.contains(STORY_DEMARCATION)) {
                story = story.replace(STORY_DEMARCATION, "");
            } else if (story.contains(novostiRossiy.getmAuthor())) {
                story = story.replace(novostiRossiy.getmAuthor(), "");
            }
        }
        holder.titleTextView.setText(story);
        return convertView;
    }
}