package dev.tudorflorea.numberfacts.utilities;

import dev.tudorflorea.numberfacts.data.Fact;

/**
 * Created by tudor on 25.03.2018.
 */

public class InterfaceUtils {
    public interface FactListener {
        void onFactRetrieved(Fact fact);
    }

    public interface FavoriteFactListener {
        void onFavoriteFactClick(Fact fact, Class activity);
    }
}
