package com.ashish.classroomelite.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.ashish.classroomelite.databinding.FragmentLoginBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class LoginFragment extends Fragment {
    String phone;
    Boolean student = false;
    Boolean faculty = false;
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private String verificationId, countryCode;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            binding.progressbar.setVisibility(View.GONE);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                binding.getotpnumber.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        countryCode = binding.countrycodepicker.getDefaultCountryCodeWithPlus();
        binding.countrycodepicker.setOnCountryChangeListener(() -> countryCode = binding.countrycodepicker.getSelectedCountryCodeWithPlus());

//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//
//        builder.setMessage("No Internet Connection!");
//        builder.setTitle("Alert !");
//        builder.setCancelable(true);
//        AlertDialog alertDialog = builder.create();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            alertDialog.cancel();
//        } else {
//            alertDialog.show();
//        }
        binding.studentButton.setOnClickListener(v -> {
            student = true;
            faculty = false;
            binding.facultyButton.setBackgroundColor(Color.TRANSPARENT);
            binding.sendotpButton.setVisibility(View.VISIBLE);
            binding.studentButton.setBackgroundColor(Color.RED);
        });
        binding.facultyButton.setOnClickListener(v -> {
            student = false;
            faculty = true;
            binding.studentButton.setBackgroundColor(Color.TRANSPARENT);
            binding.sendotpButton.setVisibility(View.VISIBLE);
            binding.facultyButton.setBackgroundColor(Color.RED);
        });

        binding.sendotpButton.setOnClickListener(v -> {
            if (Objects.requireNonNull(binding.getphonenumber.getText()).toString().length() != 10) {
                binding.phoneNumberlayout.setHelperText("Must be 10 Digits");
            } else {

                phone = countryCode + binding.getphonenumber.getText().toString();
                binding.getphonenumber.setVisibility(View.GONE);
                binding.phoneNumberlayout.setVisibility(View.GONE);
                binding.facultyButton.setVisibility(View.GONE);
                binding.studentButton.setVisibility(View.GONE);
                binding.otpnumberlayout.setVisibility(View.VISIBLE);
                binding.progressbar.setVisibility(View.VISIBLE);
                binding.getotpnumber.setVisibility(View.VISIBLE);
                binding.verifyotpButton.setVisibility(View.VISIBLE);
                binding.sendotpButton.setClickable(false);
                sendVerificationCode(phone);
            }
        });


        binding.verifyotpButton.setOnClickListener(v -> {
            if (Objects.requireNonNull(binding.getotpnumber.getText()).toString().length() != 6) {
                binding.otpnumberlayout.setHelperText("Must be 6 Digits");
            } else if (Objects.requireNonNull(binding.getotpnumber.getText()).toString().length() == 0) {
                binding.otpnumberlayout.setHelperText("Enter 6 Digits OTP");
            } else
                verifyCode(binding.getotpnumber.getText().toString());
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        NavDirections action;
                        if (student) {
                            action = LoginFragmentDirections.actionLoginFragmentToStudentProfileFragment();
                            Navigation.findNavController(requireView()).navigate(action);
                        } if(faculty) {
                            action = LoginFragmentDirections.actionLoginFragmentToFacultyProfileFragment();
                            Navigation.findNavController(requireView()).navigate(action);
                        }
                    } else {
                        Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)         // Phone number to verify
                        .setTimeout(90L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)         // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }
}