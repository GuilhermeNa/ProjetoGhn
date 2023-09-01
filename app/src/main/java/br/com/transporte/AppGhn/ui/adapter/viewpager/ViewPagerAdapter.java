package br.com.transporte.AppGhn.ui.adapter.viewpager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaAbastecimentoFragment;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaCustosDePercursoFragment;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaFreteFragment;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaResumoFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private int cavaloId;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Integer cavaloId) {
        super(fragmentActivity);
        this.cavaloId = cavaloId;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putInt("chave_id_cavalo", cavaloId);

        switch (position) {
            case 0:
                fragment = new AreaMotoristaResumoFragment();
                fragment.setArguments(bundle);
                return fragment;

            case 1:
                fragment = new AreaMotoristaFreteFragment();
                fragment.setArguments(bundle);
                return fragment;

            case 2:
                fragment = new AreaMotoristaAbastecimentoFragment();
                fragment.setArguments(bundle);
                return fragment;

            case 3:
                fragment = new AreaMotoristaCustosDePercursoFragment();
                fragment.setArguments(bundle);
                return fragment;

            default:
                fragment = new AreaMotoristaResumoFragment();
                fragment.setArguments(bundle);
                return fragment;
        }
    }


}
