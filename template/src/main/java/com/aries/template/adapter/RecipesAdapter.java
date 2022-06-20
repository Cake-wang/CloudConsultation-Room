package com.aries.template.adapter;

import com.aries.template.R;
import com.aries.template.entity.GetConsultsAndRecipesResultEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;

/**
 * @Author: AriesHoo on 2018/8/10 9:51
 * @E-Mail: AriesHoo@126.com
 * Function: 描述性条目适配器
 * Description:
 */
public class RecipesAdapter extends BaseQuickAdapter<GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail, BaseViewHolder> {

    public RecipesAdapter() {
        super(R.layout.item_recipes, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail item) {



        helper
//                .setText(R.id.tv_name, item.getName())
//                .setText(R.id.tv_drugName, item.getDrugName())
                .setText(R.id.tv_useDose, item.getUseDose()+"")
               ;
//                .setText(R.id.tv_time, item.getReportTime());

    }

}
