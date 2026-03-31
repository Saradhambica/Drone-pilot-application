package com.superbee.aeronautics

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.superbee.aeronautics.databinding.DialogEditFarmerBinding

class EditFarmerDialogFragment : DialogFragment() {

    private lateinit var binding: DialogEditFarmerBinding
    private val db = FirebaseFirestore.getInstance()

    private val stateDistrictMap = mapOf(
        "Andhra Pradesh" to listOf("Srikakulam (SR)", "Parvathipuram Manyam (PM)", "Vizianagaram (VZ)", "Visakhapatnam (VS)", "Alluri Sitharama Raju (AS)", "Anakapalli (AK)", "Kakinada (KK)", "East Godavari (EG)", "Dr. B. R. Ambedkar Konaseema (KN)", "Eluru (EL)", "West Godavari (WG)"),
        "Arunachal Pradesh" to listOf("Bichom", "Pakke-Kessang", "Kurung Kumey", "Itanagar", "Kra Daadi", "Kamle", "Keyi Panyor", "Shi-Yomi", "Siang", "Lower Siang", "Lepa-Rada", "Dibang Valley", "Namsai", "Anjaw"),
        "Assam" to listOf("Baksa", "Bajali", "Barpeta", "Biswanath", "Bongaigaon", "Cachar", "Charaideo", "Chirang", "Darrang", "Dhemaji", "Dhubri", "Dibrugarh", "Dima Hasao", "Goalpara", "Golaghat", "Hailakandi", "Hojai" ),
        "Chhattisgarh" to listOf("Balod", "Baloda Bazar", "Balrampur", "Bastar", "Bemetara", "Bijapur", "Bilaspur", "Dantewada", "Dhamtari", "Durg", "Gariaband", "Gaurella-Pendra-Marwahi", "Janjgir-Champa", "Jashpur", "Kabirdham", "Kanker"),
        "Goa" to listOf("North Goa", "South Goa"),
        "Gujarat" to listOf("Ahmedabad", "Amreli", "Anand", "Aravalli", "Banaskantha", "Bharuch", "Bhavnagar", "Botad", "Chhota Udaipur", "Dahod", "Dang", "Devbhumi Dwarka", "Gandhinagar", "Gir Somnath", "Jamnagar", "Junagadh"),
        "Haryana" to listOf("Ambala", "Bhiwani", "Charkhi Dadri", "Faridabad", "Fatehabad", "Gurugram", "Hisar", "Jhajjar", "Jind", "Kaithal", "Karnal"),
        "Himachal Pradesh" to listOf("Bilaspur", "Chamba", "Hamirpur", "Kangra", "Kinnaur", "Kullu"),
        "Jharkhand" to listOf("Bokaro", "Chatra", "Deoghar", "Dhanbad", "Dumka", "East Singhbhum", "Garhwa", "Giridih", "Godda", "Gumla", "Hazaribagh", "Jamtara"),
        "Karnataka" to listOf("Bagalkot", "Ballari (Bellary)", "Belagavi (Belgaum)", "Bengaluru (Bangalore) Rural", "Bengaluru (Bangalore) Urban", "Bidar", "Chamarajanagar", "Chikkamagaluru (Chikmagalur)", "Chitradurga", "Dakshina Kannada", "Davangere", "Dharwad", "Gadag", "Hassan", "Vijayapura (Bijapur)"),
        "Kerala" to listOf("Alappuzha", "Ernakulam", "Idukki", "Kannur", "Kasaragod", "Kollam", "Kottayam"),
        "Madhya Pradesh" to listOf("Agar Malwa", "Alirajpur", "Anuppur", "Ashoknagar", "Balaghat", "Barwan", "Betul", "Bhind", "Bhopal", "Burhanpur", "Chhatarpur", "Chhindwara", "Damoh", "Datia", "Dewas", "Dhar", "Dindori", "East Nimar", "Guna", "Gwalior", "Harda", "Hoshangabad", "Indore", "Jabalpur", "Jhabua", "Katni"),
        "Maharashtra" to listOf("Ahmednagar", "Akola", "Amravati", "Aurangabad", "Beed", "Bhandara", "Buldhana", "Chandrapur", "Dhule", "Gadchiroli", "Gondia", "Hingoli", "Jalgaon", "Jalna", "Kolhapur", "Latur", "Mumbai City", "Mumbai Suburban", "Nagpur", "Nanded", "Nandurbar", "Nashik", "Osmanabad", "Palghar", "Parbhani", "Pune", "Raigad", "Ratnagiri", "Sangli", "Satara", "Sindhudurg", "Solapur", "Thane", "Wardha", "Washim", "Yavatmal"),
        "Manipur" to listOf("Bishnupur", "Chandel", "Churachandpur", "Imphal East", "Imphal West", "Jiribam", "Kakching", "Kamjong", "Kangpokpi", "Noney", "Pherzawl", "Senapati", "Tamenglong", "Tengnoupal", "Thoubal", "Ukhrul"),
        "Meghalaya" to listOf("East Garo Hills", "East Jaintia Hills", "East Khasi Hills", "North Garo Hills", "Ri-Bhoi", "South Garo Hills", "South West Garo Hills", "South West Khasi Hills", "West Garo Hills", "West Jaintia Hills", "West Khasi Hills"),
        "Mizoram" to listOf("Aizawl", "Champhai", "Hnahthial", "Khawzawl", "Kolasib", "Lawngtlai", "Lunglei", "Mamit", "Saiha", "Saitual", "Serchhip"),
        "Nagaland" to listOf("Chümoukedima", "Dimapur", "Kiphire", "Kohima", "Longleng", "Mokokchung", "Mon", "Niuland", "Noklak", "Peren", "Phek", "Shamator", "Tuensang", "Tseminyu", "Wokha", "Zunheboto"),
        "Odisha" to listOf("Angul", "Balangir", "Balasore", "Bargarh", "Bhadrak", "Boudh", "Cuttack", "Deogarh", "Dhenkanal", "Gajapati", "Ganjam", "Jagatsinghpur", "Jajpur", "Jharsuguda", "Kalahandi", "Kandhamal", "Kendrapara", "Kendujhar", "Khordha", "Koraput", "Malkangiri", "Mayurbhanj", "Nabarangpur", "Nayagarh", "Nuapada", "Puri", "Rayagada", "Sambalpur", "Sonepur", "Sundargarh"),
        "Punjab" to listOf("Amritsar", "Barnala", "Bathinda", "Faridkot", "Fatehgarh Sahib", "Fazilka", "Ferozepur", "Gurdaspur", "Hoshiarpur", "Jalandhar", "Kapurthala", "Ludhiana", "Mansa", "Moga", "Muktsar", "Nawanshahr (Shaheed Bhagat Singh Nagar)", "Pathankot", "Patiala", "Rupnagar", "Sangrur", "Tarn Taran"),
        "Rajasthan" to listOf("Ajmer", "Alwar", "Banswara", "Baran", "Barmer", "Bharatpur", "Bhilwara", "Bikaner", "Bundi", "Chittorgarh", "Churu", "Dausa", "Dholpur", "Dungarpur", "Hanumangarh", "Jaipur", "Jaisalmer", "Jalore", "Jhalawar", "Jhunjhunu", "Jodhpur", "Karauli", "Kota", "Nagaur", "Pali", "Pratapgarh", "Rajsamand", "Sawai Madhopur", "Sikar", "Sirohi", "Sri Ganganagar", "Tonk", "Udaipur"),
        "Sikkim" to listOf("East Sikkim", "North Sikkim", "South Sikkim", "West Sikkim"),
        "Tamil Nadu" to listOf("Ariyalur", "Chengalpattu", "Chennai", "Coimbatore", "Cuddalore", "Dharmapuri", "Dindigul", "Erode", "Kallakurichi", "Kancheepuram", "Karur", "Krishnagiri", "Madurai", "Nagapattinam", "Namakkal", "Nilgiris", "Perambalur", "Pudukkottai", "Ramanathapuram", "Ranipet", "Salem", "Sivaganga", "Tenkasi", "Thanjavur", "Theni", "Thoothukudi (Tuticorin)", "Tiruchirappalli", "Tirunelveli", "Tirupattur", "Tiruppur", "Tiruvallur", "Tiruvannamalai", "Tiruvarur", "Vellore", "Viluppuram", "Virudhunagar"),
        "Telangana" to listOf(
            "Adilabad", "Bhadradri Kothagudem", "Hyderabad", "Jagtial", "Jangaon",
            "Jayashankar Bhupalpally", "Jogulamba Gadwal", "Kamareddy", "Karimnagar",
            "Khammam", "Kumuram Bheem Asifabad", "Mahabubabad", "Mahbubnagar",
            "Mancherial", "Medak", "Medchal-Malkajgiri", "Mulugu", "Nagarkurnool",
            "Nalgonda", "Narayanpet", "Nirmal", "Nizamabad", "Peddapalli",
            "Rajanna Sircilla", "Ranga Reddy", "Sangareddy", "Siddipet", "Suryapet",
            "Vikarabad", "Wanaparthy", "Warangal (Rural)", "Warangal (Urban)",
            "Yadadri Bhuvanagiri"
        ),

        "Tripura" to listOf("Dhalai", "Gomati", "Khowai", "North Tripura", "Sepahijala", "South Tripura", "Unakoti", "West Tripura"),

        "Uttar Pradesh" to listOf(
            "Agra", "Aligarh", "Ambedkar Nagar", "Amethi (Chatrapati Sahuji Mahraj Nagar)",
            "Amroha (J.P. Nagar)", "Auraiya", "Ayodhya (Faizabad)", "Azamgarh",
            "Baghpat", "Bahraich", "Ballia", "Balrampur", "Banda", "Barabanki",
            "Bareilly", "Basti", "Bhadohi (Sant Ravidas Nagar)", "Bijnor", "Budaun",
            "Bulandshahr", "Chandauli", "Chitrakoot", "Deoria", "Etah", "Etawah",
            "Farrukhabad", "Fatehpur", "Firozabad", "Gautam Buddh Nagar", "Ghaziabad",
            "Ghazipur", "Gonda", "Gorakhpur", "Hamirpur", "Hapur (Panchsheel Nagar)",
            "Hardoi", "Hathras", "Jalaun", "Jaunpur", "Jhansi", "Kannauj",
            "Kanpur Dehat (Ramabai Nagar)", "Kanpur Nagar", "Kasganj", "Kaushambi",
            "Kheri (Lakhimpur Kheri)", "Kushinagar", "Lalitpur", "Lucknow", "Mahoba",
            "Mainpuri", "Mathura", "Mau", "Meerut", "Mirzapur", "Moradabad",
            "Muzaffarnagar", "Pilibhit", "Pratapgarh", "Prayagraj (Allahabad)",
            "Raebareli", "Rampur", "Saharanpur", "Sambhal (Bhim Nagar)",
            "Sant Kabir Nagar", "Shahjahanpur", "Shamli", "Shravasti", "Siddharthnagar",
            "Sitapur", "Sonbhadra", "Sultanpur", "Unnao", "Varanasi", "Ayodhya"
        ),

        "Uttarakhand" to listOf(
            "Almora", "Bageshwar", "Chamoli", "Champawat", "Dehradun",
            "Haridwar", "Nainital", "Pauri Garhwal", "Pithoragarh", "Rudraprayag",
            "Tehri Garhwal", "Udham Singh Nagar", "Uttarkashi"
        ),

        "West Bengal" to listOf(
            "Alipurduar", "Bankura", "Birbhum", "Cooch Behar", "Dakshin Dinajpur",
            "Darjeeling", "Hooghly", "Howrah", "Jalpaiguri", "Jhargram", "Kalimpong",
            "Kolkata", "Malda", "Murshidabad", "Nadia", "North 24 Parganas",
            "Paschim Bardhaman", "Paschim Medinipur", "Purba Bardhaman",
            "Purba Medinipur", "Purulia", "South 24 Parganas", "Uttar Dinajpur"),

        )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditFarmerBinding.inflate(LayoutInflater.from(context))
        val farmer = arguments?.getParcelable<Farmer>("farmer")!!

        // ✅ Prefill existing data
        binding.editName.setText(farmer.Name)
        binding.editPhone.setText(farmer.Phone)
        binding.autoState.setText(farmer.State)
        binding.autoDistrict.setText(farmer.District)
        binding.autoVillage.setText(farmer.Village)  // changed
        binding.editPincode.setText(farmer.Pincode)

        setupDropdowns()

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Farmer Details")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                val updates = mapOf(
                    "Name" to binding.editName.text.toString().trim(),
                    "Phone" to binding.editPhone.text.toString().trim(),
                    "State" to binding.autoState.text.toString().trim(),
                    "District" to binding.autoDistrict.text.toString().trim(),
                    "Village" to binding.autoVillage.text.toString().trim(), // changed
                    "Pincode" to binding.editPincode.text.toString().trim()
                )
                db.collection("farmers").document(farmer.id).update(updates)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            val window = dialog.window
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            val params = window?.attributes
            params?.gravity = Gravity.TOP
            params?.y = 100
            window?.attributes = params
        }

        return dialog
    }

    private fun setupDropdowns() {
        val stateList = stateDistrictMap.keys.toList()
        binding.autoState.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, stateList)
        )

        binding.autoState.setOnItemClickListener { _, _, position, _ ->
            val state = stateList[position]
            val districts = stateDistrictMap[state] ?: emptyList()
            binding.autoDistrict.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, districts)
            )
            binding.autoDistrict.text.clear()
            binding.autoVillage.text?.clear() // clear text field instead of dropdown
        }

        binding.autoDistrict.setOnItemClickListener { _, _, _, _ ->
            // Optional: auto-fill village text if you want — or leave it blank
            binding.autoVillage.setText("") // user can type freely
        }

        // Always show dropdown for state/district only
        binding.autoState.setOnClickListener { binding.autoState.showDropDown() }
        binding.autoDistrict.setOnClickListener { binding.autoDistrict.showDropDown() }
    }

    companion object {
        fun newInstance(farmer: Farmer): EditFarmerDialogFragment {
            return EditFarmerDialogFragment().apply {
                arguments = Bundle().apply { putParcelable("farmer", farmer) }
            }
        }
    }
}
