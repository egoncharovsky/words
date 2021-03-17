package ru.egoncharovsky.words.ui.importing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentImportWordsBinding
import ru.egoncharovsky.words.ui.RequestCode
import ru.egoncharovsky.words.ui.observe


@AndroidEntryPoint
open class ImportWordsFragment : Fragment() {

    private val binding: FragmentImportWordsBinding by viewBinding()
    private val importWordsViewModel: ImportWordsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_import_words, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.supportedFormats.text = String.format(getString(R.string.supported_formats), "CSV")

        observe(importWordsViewModel.isImported()) {
            if (it) {
                Snackbar.make(requireParentFragment().requireView(), "Successfully imported", Snackbar.LENGTH_LONG).show()
            }
        }
        observe(importWordsViewModel.isCleared()) {
            if (it) {
                Snackbar.make(requireParentFragment().requireView(), "Database cleared", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.importing.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("text/csv", "text/comma-separated-values"))
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a CSV file"), RequestCode.SELECT_WORDS_FILE)
        }
        binding.clearDatabase.setOnClickListener {
            importWordsViewModel.clearDatabase()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.SELECT_WORDS_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                context?.contentResolver?.openInputStream(uri)?.let { importWordsViewModel.importCsv(it) }
                Snackbar.make(requireParentFragment().requireView(), "Import started", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}