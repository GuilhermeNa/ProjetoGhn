package br.com.transporte.appGhn.ui.adapter.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import br.com.transporte.appGhn.ui.fragment.desempenho.dialog.DesempenhoCustosFragment;
import br.com.transporte.appGhn.ui.fragment.desempenho.dialog.DesempenhoDespesasFragment;
import br.com.transporte.appGhn.ui.fragment.desempenho.dialog.DesempenhoReceitaFragment;

public class ViewPagerDesempenhoAdapter extends FragmentStateAdapter {

    public ViewPagerDesempenhoAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new DesempenhoReceitaFragment();
                return fragment;

            case 1:
                fragment = new DesempenhoCustosFragment();
                return fragment;

            case 2:
                fragment = new DesempenhoDespesasFragment();
                return fragment;

            default:
                return new DesempenhoReceitaFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
