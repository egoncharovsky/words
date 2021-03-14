package ru.egoncharovsky.words.ui.importing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.egoncharovsky.words.ui.RequestCode


@AndroidEntryPoint
open class ImportWordsFragment : Fragment() {

    private val importWordsViewModel: ImportWordsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent()
            .setType("text/comma-separated-values")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select a CSV file"), RequestCode.SELECT_WORDS_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.SELECT_WORDS_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                context?.contentResolver?.openInputStream(uri)?.let { importWordsViewModel.importCsv(it) }
                Snackbar.make(requireParentFragment().requireView(), "Successfully imported", Snackbar.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }
    }
}