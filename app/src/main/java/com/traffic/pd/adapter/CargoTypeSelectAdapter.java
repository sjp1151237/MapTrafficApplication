package com.traffic.pd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.traffic.pd.R;
import java.util.List;

public class CargoTypeSelectAdapter extends RecyclerView.Adapter<CargoTypeSelectAdapter.CargoHolder> {

    List<String> listCargo;
    Context context;
    CargoTypeSelect cargoTypeSelect;
    CargoLoadSelect cargoLoadSelect;

    public CargoTypeSelectAdapter(List<String> listCargo,Context context,CargoTypeSelect cargoTypeSelect,CargoLoadSelect cargoLoadSelect){
        this.listCargo = listCargo;
        this.context = context;
        this.cargoLoadSelect = cargoLoadSelect;
        this.cargoTypeSelect = cargoTypeSelect;

    }
    @NonNull
    @Override
    public CargoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cargo_item, viewGroup, false);
        return new CargoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CargoHolder cargoHolder, final int i) {
        cargoHolder.cargo_item.setText(listCargo.get(i));

        cargoHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listCargo.size() > 2){
                    cargoTypeSelect.cargoTypeSelect(listCargo.get(i));
                }else{
                    cargoLoadSelect.cargoLoadSelect(listCargo.get(i));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listCargo.size();
    }

    class CargoHolder extends RecyclerView.ViewHolder{

        TextView cargo_item;
        public CargoHolder(@NonNull View itemView) {
            super(itemView);
            cargo_item = itemView.findViewById(R.id.cargo_item);
        }
    }

    public interface CargoTypeSelect{
        void cargoTypeSelect(String name);
    }

    public interface CargoLoadSelect{
        void cargoLoadSelect(String name);
    }
}
