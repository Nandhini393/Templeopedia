package com.aryvart.templeopedia.genericclasses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.aryvart.templeopedia.Interface.GetTempleLocation;
import com.aryvart.templeopedia.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

/**
 * Created by android3 on 30/6/16.
 */
public class LocationAutoComplete extends PlaceAutocompleteFragment {
    TextView tv;
    // ImageView im;
    Marker mark;
    Double lat = 0.0, longi = 0.0;
    LatLng TutorialsPoints = new LatLng(lat, longi);
    LocationAutoComplete customPlaceAutoCompleteFragment;
    Place places;
    GeneralData gD;
    GoogleMap googleMap;
    String str_address;
    String str_lat, str_long;
    ProgressDialog dialog;
    GetTempleLocation templeLocation;
    private EditText editSearch;
    private View zzaRh;
    private View zzaRi;
    private EditText zzaRj;
    @Nullable
    private LatLngBounds zzaRk;
    @Nullable
    private AutocompleteFilter zzaRl;
    @Nullable
    private PlaceSelectionListener zzaRm;

    public LocationAutoComplete() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View var4 = inflater.inflate(R.layout.providerplaceautocomplet, container, false);
        //   im = (ImageView) var4.findViewById(R.id.search_icon);
        // tv = (TextView) var4.findViewById(R.id.my_text);

        editSearch = (EditText) var4.findViewById(R.id.editWorkLocation);
        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearch.setText("");
                zzzG();
            }
        });
        str_address = editSearch.getText().toString();

        Log.i("GB", "DDDD : " + str_address);

     /*   im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zzzG();
                editSearch.setVisibility(View.VISIBLE);
                //  tv.setVisibility(View.INVISIBLE);
                im.setVisibility(View.INVISIBLE);
            }
        });*/

       /* editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zzzG();
            }

        });*/
        return var4;
    }


    public void onDestroyView() {
        this.zzaRh = null;
        this.zzaRi = null;
        this.editSearch = null;
        super.onDestroyView();
    }

    public void setBoundsBias(@Nullable LatLngBounds bounds) {
        this.zzaRk = bounds;
    }

    public void setFilter(@Nullable AutocompleteFilter filter) {
        this.zzaRl = filter;
    }

    public void setText(CharSequence text) {
        this.editSearch.setText(text);
        //this.zzzF();
    }

    public void setHint(CharSequence hint) {
        this.editSearch.setHint(hint);
        this.zzaRh.setContentDescription(hint);
    }

    public void setOnPlaceSelectedListener(PlaceSelectionListener listener) {
        this.zzaRm = listener;


    }

    private void zzzF() {
        boolean var1 = !this.editSearch.getText().toString().isEmpty();
        //this.zzaRi.setVisibility(var1?0:8);
    }

    private void zzzG() {

        int var1 = -1;

        try {
            Intent var2 = (new PlaceAutocomplete.IntentBuilder(2)).setBoundsBias(this.zzaRk).setFilter(this.zzaRl).zzku(this.editSearch.getText().toString()).zzuo(1).build(this.getActivity());

            this.startActivityForResult(var2, 1);
        } catch (GooglePlayServicesRepairableException var3) {
            var1 = var3.getConnectionStatusCode();
            Log.e("Places", "Could not open autocomplete activity", var3);
        } catch (GooglePlayServicesNotAvailableException var4) {
            var1 = var4.errorCode;
            Log.e("Places", "Could not open autocomplete activity", var4);
        }

        if (var1 != -1) {
            GoogleApiAvailability var5 = GoogleApiAvailability.getInstance();
            var5.showErrorDialogFragment(this.getActivity(), var1, 2);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

            Log.e("PL", "i'm in onActivityResult 1 : " + resultCode);

            if (resultCode == -1) {

                Log.e("PL", "i'm in onActivityResult 2 : " + resultCode);

                Place var4 = PlaceAutocomplete.getPlace(this.getActivity(), data);
                if (mark != null) {
                    mark.remove();
                }


                gD = new GeneralData();



                /*//place marker
                TutorialsPoints = new LatLng(Double.parseDouble(String.valueOf(var4.getLatLng().latitude)), Double.parseDouble(String.valueOf(var4.getLatLng().longitude)));
                if (mark != null) {
                    mark.remove();
                }
*/
                //Get the Address from the lat and lang

                String result = "";
                try {
                    Geocoder geocoder;
                    List<Address> addressList = null;
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    addressList = geocoder.getFromLocation(Double.parseDouble(String.valueOf(var4.getLatLng().latitude)), Double.parseDouble(String.valueOf(var4.getLatLng().longitude)), 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append(",");
                        }
                        sb.append(address.getLocality()).append(",");
                        sb.append(address.getPostalCode()).append(",");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                        Log.e("AddrVal", result);

                        str_lat = String.valueOf(Double.parseDouble(String.valueOf(var4.getLatLng().latitude)));
                        str_long = String.valueOf(Double.parseDouble(String.valueOf(var4.getLatLng().longitude)));
                        //   editSearch.setText(result);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*mark = gD.prov_googleMapGeneral.addMarker(new MarkerOptions().position(TutorialsPoints).snippet(result).title("hii").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

                gD.prov_googleMapGeneral.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(TutorialsPoints)      // Sets the center of the map to Mountain View
                                .zoom(15)
                                .tilt(90)
                                        // Sets the zoom// Sets the orientation of the camera to east// Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        gD.prov_googleMapGeneral.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mark.showInfoWindow();
                        return false;
                    }
                });

*/
                if (this.zzaRm != null) {
                    this.zzaRm.onPlaceSelected(var4);
                    Log.e("PL", "" + var4);
                }

                Log.i("GGG", "CustomPlaceAutoCompleteFragment : " + var4.getAddress().toString());


                gD.pro_searchAddress = var4.getAddress().toString();
                gD.pro_searchLatitutde = String.valueOf(var4.getLatLng().latitude);
                gD.pro_searchLongitude = String.valueOf(var4.getLatLng().longitude);

                //gD.strAddress= var4.getAddress().toString();

                // this.setText(var4.getName().toString());
                this.setText("");
            } else if (resultCode == 2) {
                Status var5 = PlaceAutocomplete.getStatus(this.getActivity(), data);
                if (this.zzaRm != null) {
                    this.zzaRm.onError(var5);
                    Log.e("place---->", "" + var5);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void drawMarker(LatLng point, final String str_address, final String strProviderId, final String strResp) {
        try {
            // Creating an instance of MarkerOptions
            final MarkerOptions markerOptions = new MarkerOptions();
            gD.googleMapGeneral.setInfoWindowAdapter(new MyInfoWindowAdapter());
            // Setting latitude and longitude for the marker
            markerOptions.position(point).snippet(str_address).title(strProviderId).icon(BitmapDescriptorFactory.fromResource(R.drawable.markergreen));
            // Adding marker on the Google Map

            googleMap.addMarker(markerOptions);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.user_current_loc_addr_alert, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.text_address));
            tvSnippet.setText(marker.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }
    }
}