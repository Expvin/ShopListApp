package com.example.shoplistapp.presentation


import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoplistapp.R
import com.example.shoplistapp.ShopApplication
import com.example.shoplistapp.databinding.ShopItemFragmentBinding
import com.example.shoplistapp.domain.ShopItem
import javax.inject.Inject
import kotlin.concurrent.thread

class ShopItemFragment: Fragment() {

    private var _binding: ShopItemFragmentBinding? = null
    private val binding: ShopItemFragmentBinding
        get() = _binding ?: throw RuntimeException("ViewBinding = null")
    lateinit var onItemEditingFinishedListener: OnItemEditingFinishedListener
    private lateinit var viewModel: ShopItemViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var screenMode: String = MODE_UNKNOWN
    private var itemId: Int = ShopItem.NOT_SPECIFIED_ID
    private val component by lazy {
        (requireActivity().application as ShopApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParam()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ShopItemFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addTextChangeListener()
        launchRightMode()
        errorListener()
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnItemEditingFinishedListener) {
            onItemEditingFinishedListener = context
        } else throw RuntimeException("Not implement OnItemEditingFinish")
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchModeAdd()
            MODE_EDIT -> launchModeEdit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun errorListener() {
        viewModel.shouldCloseActivity.observe(viewLifecycleOwner) {
            onItemEditingFinishedListener.onItemEditingFinish()
        }
    }

    private fun addTextChangeListener() {
        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.countEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun launchModeAdd() {
        binding.saveButton.setOnClickListener {
//            viewModel.addShopItem(
//                binding.nameEditText.text?.toString(),
//                binding.countEditText.text?.toString()
//            )
            thread {
                context?.contentResolver?.insert(Uri.parse("content://com.example.shoplistapp/shop_list"),
                    ContentValues().apply {
                        put("id", 0)
                        put("name", binding.nameEditText.text?.toString())
                        put("count", binding.countEditText.text?.toString()?.toInt())
                        put("enabled", true)
                    })
            }
        }
    }

    private fun launchModeEdit() {
        viewModel.getShopItem(itemId)
        binding.saveButton.setOnClickListener {
//            viewModel.updateShopItem(
//                binding.nameEditText.text?.toString(),
//                binding.countEditText.text?.toString()
//            )
            thread {
                context?.contentResolver?.update(Uri.parse("content://com.example.shoplistapp/shop_list"),
                    ContentValues().apply {
                        put("id", itemId)
                        put("name", binding.nameEditText.text?.toString())
                        put("count", binding.countEditText.text?.toString()?.toInt())
                        put("enabled", true)
                    }, null, null)
            }
        }
    }

    private fun parseParam() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) throw RuntimeException("Param screen mode is absent")
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) throw RuntimeException("Unknown mode $mode")
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) throw RuntimeException("Param shop item id is absent")
            itemId = args.getInt(SHOP_ITEM_ID, ShopItem.NOT_SPECIFIED_ID)
        }
    }

    interface OnItemEditingFinishedListener {
        fun onItemEditingFinish()
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newInstanceEditItem(itemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, itemId)
                }
            }
        }

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }
    }
}
