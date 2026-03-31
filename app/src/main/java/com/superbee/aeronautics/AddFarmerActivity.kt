package com.superbee.aeronautics

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class AddFarmerActivity : AppCompatActivity() {

    private lateinit var editName: TextInputEditText
    private lateinit var editPhone: TextInputEditText
    private lateinit var autoState: androidx.appcompat.widget.AppCompatAutoCompleteTextView
    private lateinit var autoDistrict: androidx.appcompat.widget.AppCompatAutoCompleteTextView
    private lateinit var editVillage: TextInputEditText
    private lateinit var editPincode: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_farmer)

        firestore = FirebaseFirestore.getInstance()

        editName = findViewById(R.id.editName)
        editPhone = findViewById(R.id.editPhone)
        autoState = findViewById(R.id.autoState)
        autoDistrict = findViewById(R.id.autoDistrict)
        editVillage = findViewById(R.id.autoVillage)
        editPincode = findViewById(R.id.editPincode)
        btnSave = findViewById(R.id.btnSave)

        setupDropdowns()
        setupSaveButton()
    }

    private fun setupDropdowns() {
        val stateDistrictMap = mapOf(
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

        val stateList = stateDistrictMap.keys.toList()

        // ✅ use `this` (Activity context), not `requireContext()`
        val stateAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, stateList)
        autoState.setAdapter(stateAdapter)

        autoState.setOnItemClickListener { _, _, position, _ ->
            val selectedState = stateList[position]
            val districts = stateDistrictMap[selectedState] ?: emptyList()
            val districtAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, districts)
            autoDistrict.setAdapter(districtAdapter)
            autoDistrict.text.clear()
        }

        // Show dropdown when clicked
        autoState.setOnClickListener { autoState.showDropDown() }
        autoDistrict.setOnClickListener { autoDistrict.showDropDown() }
    }


    private fun setupSaveButton() {
        btnSave.setOnClickListener {
            val name = editName.text?.toString()?.trim().orEmpty()
            val phone = editPhone.text?.toString()?.trim().orEmpty()
            val state = autoState.text?.toString()?.trim().orEmpty()
            val district = autoDistrict.text?.toString()?.trim().orEmpty()
            val village = editVillage.text?.toString()?.trim().orEmpty()
            val pincode = editPincode.text?.toString()?.trim().orEmpty()

            if (name.isEmpty() || phone.isEmpty() || state.isEmpty() ||
                district.isEmpty() || village.isEmpty() || pincode.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Add timestamp for ordering
            val farmer = hashMapOf(
                "Name" to name,
                "Phone" to phone,
                "State" to state,
                "District" to district,
                "Village" to village,
                "Pincode" to pincode,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("farmers")
                .add(farmer)
                .addOnSuccessListener {
                    Toast.makeText(this, "Farmer added successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
