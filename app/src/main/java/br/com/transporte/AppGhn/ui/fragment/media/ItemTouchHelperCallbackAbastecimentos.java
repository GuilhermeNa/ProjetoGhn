package br.com.transporte.AppGhn.ui.fragment.media;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import br.com.transporte.AppGhn.ui.adapter.MediaAdapter_Abastecimentos;

public class ItemTouchHelperCallbackAbastecimentos extends ItemTouchHelper.Callback {

    private final MediaAdapter_Abastecimentos adapter;

    public ItemTouchHelperCallbackAbastecimentos(MediaAdapter_Abastecimentos adapter) {
        this.adapter = adapter;
    }

    //----------------------------------------------------------------------------------------------
    //                                         Movements                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int deslizaHorizontal = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(0, deslizaHorizontal);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.escondeItem(viewHolder.getAdapterPosition());
    }
}
