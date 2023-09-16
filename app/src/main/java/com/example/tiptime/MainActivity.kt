package com.example.tiptime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {

    /** バインディングクラス */
    private lateinit var binding: ActivityMainBinding

    /** アクティビティ名 */
    private val activityName = this.javaClass.simpleName

    /**
     * チップの割合
     */
    private enum class TipPercentage(val value: Double) {
        TWENTY_PERCENT(0.20),
        EIGHTEEN_PERCENT(0.18),
        FIFTEEN_PERCENT(0.15)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener { calculateTip() }
    }

    /**
     * チップの金額を計算し、表示エリアにテキストをセットし、表示する
     */
    private fun calculateTip() {
        Log.i(activityName, "calculateTip() start.")

        val stringInTextField = binding.costOfService.text.toString()
        var cost = stringInTextField.toDoubleOrNull()

        // テキストが入力されていない場合表示エリアのテキストに０をセット
        if (cost == null) {
            Log.i(activityName, "Cost is Null.")
            displayTip(0.0)
            return
        }

        val tipPercentage = selectPercentage(binding.tipOption.checkedRadioButtonId)
        var tip = tipPercentage * cost

        // 切り上げボタンがチェックされていたら切り上げ
        if (isRoundUpChecked()) {
            tip = kotlin.math.ceil(tip)
        }
        displayTip(tip)

        Log.i(activityName, "calculateTip() end.")
    }

    /**
     * 何パーセントチップをあげるか判定
     *
     * @return チップの割合
     */
    private fun selectPercentage(selectedId: Int) = when (selectedId) {
        R.id.option_twenty_percent -> TipPercentage.TWENTY_PERCENT.value
        R.id.option_eighteen_percent -> TipPercentage.EIGHTEEN_PERCENT.value
        else -> TipPercentage.FIFTEEN_PERCENT.value
    }

    /**
     * 切り上げボタンがチェックされているか
     *
     * @return true 切り上げ / false 切り上げしない
     */
    private fun isRoundUpChecked() = binding.roundUpSwitch.isChecked

    /**
     * チップ額表示エリアにテキストをセットし、表示する
     */
    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }
}
