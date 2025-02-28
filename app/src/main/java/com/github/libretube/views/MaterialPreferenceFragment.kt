package com.github.libretube.views

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.github.libretube.R
import com.github.libretube.databinding.DialogTextPreferenceBinding
import com.github.libretube.preferences.PreferenceHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * PreferenceFragmentCompat using the [MaterialAlertDialogBuilder] instead of the old dialog builder
 */
open class MaterialPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {}

    override fun onDisplayPreferenceDialog(preference: Preference) {
        when (preference) {
            /**
             * Show a [MaterialAlertDialogBuilder] when the preference is a [ListPreference]
             */
            is ListPreference -> {
                // get the index of the previous selected item
                val prefIndex = preference.entryValues.indexOf(preference.value)
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(preference.title)
                    .setSingleChoiceItems(preference.entries, prefIndex) { dialog, index ->

                        // get the new ListPreference value
                        val newValue = preference.entryValues[index].toString()

                        // save the new value and call the onPreferenceChange Method
                        preference.value = newValue
                        preference.callChangeListener(newValue)

                        // dismiss the dialog
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
            is EditTextPreference -> {
                val binding = DialogTextPreferenceBinding.inflate(layoutInflater)
                binding.input.setText(
                    PreferenceHelper.getString(
                        preference.key,
                        ""
                    )
                )
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(preference.title)
                    .setView(binding.root)
                    .setPositiveButton(R.string.okay) { _, _ ->
                        // save the new value
                        preference.text = binding.input.text.toString()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
            /**
             * Otherwise show the normal dialog, dialogs for other preference types are not supported yet
             */
            else -> super.onDisplayPreferenceDialog(preference)
        }
    }
}
