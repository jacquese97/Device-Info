package com.ytheekshana.deviceinfo;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import android.util.SizeF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class tabCamera extends Fragment {
    Context context;
    private RecyclerView recyclerCamera;
    private RecyclerView.Adapter cameraAdapter;
    private ArrayList<CameraInfo> featureList2;
    private CameraManager cameraManager;
    private SingleSelectToggleGroup cameraButtonGroup;
    private int textDisColor, lineColor;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabcamera, container, false);

        try {
            textDisColor = MainActivity.themeColor;
            lineColor = GetDetails.getThemeColor(Objects.requireNonNull(getContext()), R.attr.colorButtonNormal);

            cameraButtonGroup = rootView.findViewById(R.id.cameraButtonGroup);
            recyclerCamera = rootView.findViewById(R.id.recyclerCamera);
            loadCameraAll();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rootView;
    }

    private void loadCameraAll() {
        ArrayList<CameraInfo> featureList = new ArrayList<>();
        getCameraMetaData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        cameraAdapter = new CameraAdapter(context, lineColor, textDisColor, featureList);
        recyclerCamera.setLayoutManager(layoutManager);
        recyclerCamera.setAdapter(cameraAdapter);

        featureList2 = new ArrayList<>();
        cameraButtonGroup.setOnCheckedChangeListener((group, checkedId) -> {
            final CameraButton cameraButton = group.findViewById(group.getCheckedId());
            getCameraDetails(String.valueOf(cameraButton.getCameraId()), featureList2);
            ((CameraAdapter) cameraAdapter).addData(featureList2);
            cameraAdapter.notifyDataSetChanged();
        });
    }

    private void getCameraMetaData() {
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {

            int a = 0;
            for (final String cameraId : Objects.requireNonNull(cameraManager).getCameraIdList()) {

                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);

                StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                Size[] sizes = Objects.requireNonNull(streamConfigurationMap).getOutputSizes(ImageFormat.JPEG);
                Integer lensFacing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                String lens = "";
                if (lensFacing != null) {
                    switch (lensFacing) {
                        case CameraCharacteristics.LENS_FACING_BACK:
                            lens = getString(R.string.back);
                            break;
                        case CameraCharacteristics.LENS_FACING_FRONT:
                            lens = getString(R.string.front);
                            break;
                        case CameraCharacteristics.LENS_FACING_EXTERNAL:
                            lens = getString(R.string.external);
                            break;
                        default:
                            lens = getString(R.string.unknown);
                            break;
                    }
                }
                float[] flenths = cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
                CameraButton cameraButton = new CameraButton(context);
                cameraButton.setMp(GetDetails.getCameraMP(sizes) + " - " + lens);
                cameraButton.setResolution(GetDetails.getCameraResolution(sizes));
                cameraButton.setFlength(Objects.requireNonNull(flenths)[0] + "mm");
                cameraButton.setCameraId(cameraId);
                if (a == 0) {
                    cameraButton.setChecked(true);
                }
                a++;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 0, 8, 0);
                cameraButton.setLayoutParams(params);
                cameraButtonGroup.addView(cameraButton);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void getCameraDetails(String cameraId, ArrayList<CameraInfo> list) {
        try {
            CameraCharacteristics camchar = cameraManager.getCameraCharacteristics(cameraId);
            list.clear();
            list.add(new CameraInfo(getString(R.string.cameraNoteTitle), getString(R.string.cameraNote)));
            for (CameraCharacteristics.Key key : camchar.getKeys()) {

                String featureValue = getCameraFeatureValue(key, camchar);
                if (!featureValue.trim().equals("")) {
                    list.add(new CameraInfo(GetDetails.getKeyName(key.getName(), context), featureValue));
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private String getCameraFeatureValue(CameraCharacteristics.Key key, CameraCharacteristics characteristics) {
        List<String> values = new ArrayList<>();

        if (key == CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_FAST) {
                    values.add(getString(R.string.feature_fast));
                } else if (character == CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY) {
                    values.add(getString(R.string.feature_high_quality));
                }
            }

        } else if (key == CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_AUTO) {
                    values.add(getString(R.string.auto));
                } else if (character == CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_50HZ) {
                    values.add("50Hz");
                } else if (character == CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_60HZ) {
                    values.add("60Hz");
                }
            }
        } else if (key == CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.CONTROL_AE_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.CONTROL_AE_MODE_ON) {
                    values.add(getString(R.string.on));
                } else if (character == CameraCharacteristics.CONTROL_AE_MODE_ON_ALWAYS_FLASH) {
                    values.add(getString(R.string.feature_always_flash));
                } else if (character == CameraCharacteristics.CONTROL_AE_MODE_ON_AUTO_FLASH) {
                    values.add(getString(R.string.feature_auto_flash));
                } else if (character == CameraCharacteristics.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE) {
                    values.add(getString(R.string.feature_auto_flash_redeye));
                }
            }
        } else if (key == CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES) {
            Range<Integer>[] intChar = (Range<Integer>[]) characteristics.get(key);
            assert intChar != null;
            for (Range<Integer> character : intChar) {
                values.add(getRange(character));
            }
        } else if (key == CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE) {
            Range<Integer> character = (Range<Integer>) characteristics.get(key);
            values.add(getRange(Objects.requireNonNull(character)));
        } else if (key == CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP) {
            Rational character = (Rational) characteristics.get(key);
            values.add(Objects.requireNonNull(character).toString());
        } else if (key == CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.CONTROL_AF_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.CONTROL_AF_MODE_AUTO) {
                    values.add(getString(R.string.auto));
                } else if (character == CameraCharacteristics.CONTROL_AF_MODE_EDOF) {
                    values.add(getString(R.string.feature_edof));
                } else if (character == CameraCharacteristics.CONTROL_AF_MODE_MACRO) {
                    values.add(getString(R.string.feature_macro));
                } else if (character == CameraCharacteristics.CONTROL_AF_MODE_CONTINUOUS_PICTURE) {
                    values.add(getString(R.string.feature_continuous_picture));
                } else if (character == CameraCharacteristics.CONTROL_AF_MODE_CONTINUOUS_VIDEO) {
                    values.add(getString(R.string.feature_continuous_video));
                }
            }
        } else if (key == CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.CONTROL_EFFECT_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.CONTROL_EFFECT_MODE_AQUA) {
                    values.add(getString(R.string.feature_aqua));
                } else if (character == CameraCharacteristics.CONTROL_EFFECT_MODE_BLACKBOARD) {
                    values.add(getString(R.string.feature_blackboard));
                } else if (character == CameraCharacteristics.CONTROL_EFFECT_MODE_MONO) {
                    values.add(getString(R.string.feature_mono));
                } else if (character == CameraCharacteristics.CONTROL_EFFECT_MODE_NEGATIVE) {
                    values.add(getString(R.string.feature_negative));
                } else if (character == CameraCharacteristics.CONTROL_EFFECT_MODE_POSTERIZE) {
                    values.add(getString(R.string.feature_posterize));
                } else if (character == CameraCharacteristics.CONTROL_EFFECT_MODE_SEPIA) {
                    values.add(getString(R.string.feature_sepia));
                } else if (character == CameraCharacteristics.CONTROL_EFFECT_MODE_SOLARIZE) {
                    values.add(getString(R.string.feature_solarize));
                } else if (character == CameraCharacteristics.CONTROL_EFFECT_MODE_WHITEBOARD) {
                    values.add(getString(R.string.feature_whiteboard));
                }
            }
        } else if (key == CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.CONTROL_SCENE_MODE_DISABLED) {
                    values.add(getString(R.string.disabled));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_ACTION) {
                    values.add(getString(R.string.feature_action));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_BARCODE) {
                    values.add(getString(R.string.feature_barcode));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_BEACH) {
                    values.add(getString(R.string.feature_beach));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_CANDLELIGHT) {
                    values.add(getString(R.string.feature_candlelight));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_FACE_PRIORITY) {
                    values.add(getString(R.string.feature_face_priority));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_FIREWORKS) {
                    values.add(getString(R.string.feature_fireworks));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_HDR) {
                    values.add(getString(R.string.feature_hdr));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_LANDSCAPE) {
                    values.add(getString(R.string.feature_landscape));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_NIGHT) {
                    values.add(getString(R.string.feature_night));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_NIGHT_PORTRAIT) {
                    values.add(getString(R.string.feature_night_portrait));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_PARTY) {
                    values.add(getString(R.string.feature_party));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_PORTRAIT) {
                    values.add(getString(R.string.feature_portrait));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_SNOW) {
                    values.add(getString(R.string.feature_snow));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_SPORTS) {
                    values.add(getString(R.string.feature_sports));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_STEADYPHOTO) {
                    values.add(getString(R.string.feature_steady_photo));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_SUNSET) {
                    values.add(getString(R.string.feature_sunset));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_THEATRE) {
                    values.add(getString(R.string.feature_theatre));
                } else if (character == CameraCharacteristics.CONTROL_SCENE_MODE_HIGH_SPEED_VIDEO) {
                    values.add(getString(R.string.feature_high_speed_video));
                }
            }
        } else if (key == CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.CONTROL_VIDEO_STABILIZATION_MODE_ON) {
                    values.add(getString(R.string.on));
                } else if (character == CameraCharacteristics.CONTROL_VIDEO_STABILIZATION_MODE_OFF) {
                    values.add(getString(R.string.off));
                }
            }
        } else if (key == CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.CONTROL_AWB_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.CONTROL_AWB_MODE_AUTO) {
                    values.add(getString(R.string.auto));
                } else if (character == CameraCharacteristics.CONTROL_AWB_MODE_CLOUDY_DAYLIGHT) {
                    values.add(getString(R.string.feature_cloudy_daylight));
                } else if (character == CameraCharacteristics.CONTROL_AWB_MODE_DAYLIGHT) {
                    values.add(getString(R.string.feature_daylight));
                } else if (character == CameraCharacteristics.CONTROL_AWB_MODE_FLUORESCENT) {
                    values.add(getString(R.string.feature_fluorescent));
                } else if (character == CameraCharacteristics.CONTROL_AWB_MODE_INCANDESCENT) {
                    values.add(getString(R.string.feature_incandescent));
                } else if (character == CameraCharacteristics.CONTROL_AWB_MODE_SHADE) {
                    values.add(getString(R.string.feature_shade));
                } else if (character == CameraCharacteristics.CONTROL_AWB_MODE_TWILIGHT) {
                    values.add(getString(R.string.feature_twilight));
                } else if (character == CameraCharacteristics.CONTROL_AWB_MODE_WARM_FLUORESCENT) {
                    values.add(getString(R.string.feature_warm_fluorescent));
                }
            }
        } else if (key == CameraCharacteristics.CONTROL_MAX_REGIONS_AE) {
            int character = (int) characteristics.get(key);
            values.add(String.valueOf(character));
        } else if (key == CameraCharacteristics.CONTROL_MAX_REGIONS_AF) {
            int character = (int) characteristics.get(key);
            values.add(String.valueOf(character));
        } else if (key == CameraCharacteristics.CONTROL_MAX_REGIONS_AWB) {
            int character = (int) characteristics.get(key);
            values.add(String.valueOf(character));
        } else if (key == CameraCharacteristics.EDGE_AVAILABLE_EDGE_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.EDGE_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.EDGE_MODE_FAST) {
                    values.add(getString(R.string.feature_fast));
                } else if (character == CameraCharacteristics.EDGE_MODE_HIGH_QUALITY) {
                    values.add(getString(R.string.feature_high_quality));
                } else if (character == CameraCharacteristics.EDGE_MODE_ZERO_SHUTTER_LAG) {
                    values.add(getString(R.string.feature_zero_shutter_lag));
                }
            }
        } else if (key == CameraCharacteristics.FLASH_INFO_AVAILABLE) {
            boolean character = (boolean) characteristics.get(key);
            values.add(character ? getString(R.string.yes) : getString(R.string.no));
        } else if (key == CameraCharacteristics.HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.HOT_PIXEL_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.HOT_PIXEL_MODE_FAST) {
                    values.add(getString(R.string.feature_fast));
                } else if (character == CameraCharacteristics.HOT_PIXEL_MODE_HIGH_QUALITY) {
                    values.add(getString(R.string.feature_high_quality));
                }
            }
        } else if (key == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) {
            int character = (int) characteristics.get(key);
            if (character == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3) {
                values.add(getString(R.string.feature_level_3));
            } else if (character == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL) {
                values.add(getString(R.string.external));
            } else if (character == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL) {
                values.add(getString(R.string.feature_full));
            } else if (character == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                values.add(getString(R.string.feature_legacy));
            } else if (character == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED) {
                values.add(getString(R.string.feature_limited));
            }
        } else if (key == CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES) {
            Size[] character = (Size[]) characteristics.get(key);
            for (Size size : character != null ? character : new Size[0]) {
                values.add(size.getWidth() + " x " + size.getHeight());
            }
        } else if (key == CameraCharacteristics.LENS_FACING) {
            int character = (int) characteristics.get(key);
            if (character == CameraCharacteristics.LENS_FACING_BACK) {
                values.add(getString(R.string.back));
            } else if (character == CameraCharacteristics.LENS_FACING_EXTERNAL) {
                values.add(getString(R.string.external));
            } else if (character == CameraCharacteristics.LENS_FACING_FRONT) {
                values.add(getString(R.string.front));
            } else {
                values.add(getString(R.string.unknown));
            }
        } else if (key == CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES) {
            float[] floatChar = (float[]) characteristics.get(key);
            for (float character : floatChar != null ? floatChar : new float[0]) {
                values.add(String.valueOf(character));
            }
        } else if (key == CameraCharacteristics.LENS_INFO_AVAILABLE_FILTER_DENSITIES) {
            float[] floatChar = (float[]) characteristics.get(key);
            for (float character : floatChar != null ? floatChar : new float[0]) {
                values.add(String.valueOf(character));
            }
        } else if (key == CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS) {
            float[] floatChar = (float[]) characteristics.get(key);
            for (float character : floatChar != null ? floatChar : new float[0]) {
                values.add(character + "mm");
            }
        } else if (key == CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.LENS_OPTICAL_STABILIZATION_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.LENS_OPTICAL_STABILIZATION_MODE_ON) {
                    values.add(getString(R.string.on));
                }
            }
        } else if (key == CameraCharacteristics.LENS_INFO_FOCUS_DISTANCE_CALIBRATION) {
            int character = (int) characteristics.get(key);
            if (character == CameraCharacteristics.LENS_INFO_FOCUS_DISTANCE_CALIBRATION_APPROXIMATE) {
                values.add(getString(R.string.feature_approximate));
            } else if (character == CameraCharacteristics.LENS_INFO_FOCUS_DISTANCE_CALIBRATION_CALIBRATED) {
                values.add(getString(R.string.feature_calibrated));
            } else if (character == CameraCharacteristics.LENS_INFO_FOCUS_DISTANCE_CALIBRATION_UNCALIBRATED) {
                values.add(getString(R.string.feature_uncalibrated));
            }
        } else if (key == CameraCharacteristics.LENS_INFO_HYPERFOCAL_DISTANCE) {
            float character = (float) characteristics.get(key);
            values.add(String.valueOf(character));
        } else if (key == CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE) {
            float character = (float) characteristics.get(key);
            values.add(String.valueOf(character));
        } else if (key == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE) {
                    values.add(getString(R.string.feature_backward_compatible));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_BURST_CAPTURE) {
                    values.add(getString(R.string.feature_burst_capture));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_CONSTRAINED_HIGH_SPEED_VIDEO) {
                    values.add(getString(R.string.feature_constrained_high_speed_video));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_DEPTH_OUTPUT) {
                    values.add(getString(R.string.feature_depth_output));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA) {
                    values.add(getString(R.string.feature_logical_multi_camera));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MANUAL_POST_PROCESSING) {
                    values.add(getString(R.string.feature_manual_post_processing));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MANUAL_SENSOR) {
                    values.add(getString(R.string.feature_manual_sensor));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MONOCHROME) {
                    values.add(getString(R.string.feature_monochrome));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MOTION_TRACKING) {
                    values.add(getString(R.string.feature_motion_tracking));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_PRIVATE_REPROCESSING) {
                    values.add(getString(R.string.feature_private_reprocessing));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW) {
                    values.add(getString(R.string.feature_raw));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_READ_SENSOR_SETTINGS) {
                    values.add(getString(R.string.feature_read_sensor_settings));
                } else if (character == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_YUV_REPROCESSING) {
                    values.add(getString(R.string.feature_yuv_reprocessing));
                }
            }
        } else if (key == CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC) {
            int character = (int) characteristics.get(key);
            values.add(String.valueOf(character));
        } else if (key == CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC_STALLING) {
            int character = (int) characteristics.get(key);
            values.add(String.valueOf(character));
        } else if (key == CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_RAW) {
            int character = (int) characteristics.get(key);
            values.add(String.valueOf(character));
        } else if (key == CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT) {
            int character = (int) characteristics.get(key);
            values.add(String.valueOf(character));
        } else if (key == CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM) {
            float character = (float) characteristics.get(key);
            values.add(String.valueOf(character));
        } else if (key == CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) {
            StreamConfigurationMap character = (StreamConfigurationMap) characteristics.get(key);
            Size[] intArray = Objects.requireNonNull(character).getOutputSizes(ImageFormat.JPEG);
            for (Size size : intArray) {
                values.add(GetDetails.getMP(size, 2) + " - " + size.getWidth() + " x " + size.getHeight());
            }
        } else if (key == CameraCharacteristics.SCALER_CROPPING_TYPE) {
            int character = (int) characteristics.get(key);
            if (character == CameraCharacteristics.SCALER_CROPPING_TYPE_CENTER_ONLY) {
                values.add(getString(R.string.feature_center_only));
            } else if (character == CameraCharacteristics.SCALER_CROPPING_TYPE_FREEFORM) {
                values.add(getString(R.string.feature_freeform));
            }
        } else if (key == CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.SENSOR_TEST_PATTERN_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.SENSOR_TEST_PATTERN_MODE_COLOR_BARS) {
                    values.add(getString(R.string.feature_color_bars));
                } else if (character == CameraCharacteristics.SENSOR_TEST_PATTERN_MODE_COLOR_BARS_FADE_TO_GRAY) {
                    values.add(getString(R.string.feature_color_bars_fade_to_gray));
                } else if (character == CameraCharacteristics.SENSOR_TEST_PATTERN_MODE_CUSTOM1) {
                    values.add(getString(R.string.feature_custom_1));
                } else if (character == CameraCharacteristics.SENSOR_TEST_PATTERN_MODE_PN9) {
                    values.add(getString(R.string.feature_pn9));
                } else if (character == CameraCharacteristics.SENSOR_TEST_PATTERN_MODE_SOLID_COLOR) {
                    values.add(getString(R.string.feature_solid_color));
                }
            }
        } else if (key == CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT) {
            int character = (int) characteristics.get(key);
            if (character == CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_BGGR) {
                values.add("BGGR");
            } else if (character == CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_GBRG) {
                values.add("GBRG");
            } else if (character == CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_GRBG) {
                values.add("GRGB");
            } else if (character == CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_RGB) {
                values.add("RGB");
            } else if (character == CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_RGGB) {
                values.add("RGGB");
            }
        } else if (key == CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE) {
            int character = (int) characteristics.get(key);
            if (character == CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE_REALTIME) {
                values.add(getString(R.string.feature_realtime));
            } else if (character == CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE_UNKNOWN) {
                values.add(getString(R.string.unknown));
            }
        } else if (key == CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE) {
            SizeF character = (SizeF) characteristics.get(key);
            values.add(String.format(Locale.US, "%.2f", Objects.requireNonNull(character).getWidth()) + " x " + String.format(Locale.US, "%.2f", character.getHeight()));
        } else if (key == CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE) {
            Size size = (Size) characteristics.get(key);
            values.add(Objects.requireNonNull(size).getWidth() + " x " + size.getHeight());
        } else if (key == CameraCharacteristics.SENSOR_ORIENTATION) {
            int character = (int) characteristics.get(key);
            values.add(character + " deg");
        } else if (key == CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES) {
            int[] intChar = (int[]) characteristics.get(key);
            for (int character : intChar != null ? intChar : new int[0]) {
                if (character == CameraCharacteristics.STATISTICS_FACE_DETECT_MODE_OFF) {
                    values.add(getString(R.string.off));
                } else if (character == CameraCharacteristics.STATISTICS_FACE_DETECT_MODE_FULL) {
                    values.add(getString(R.string.feature_full));
                } else if (character == CameraCharacteristics.STATISTICS_FACE_DETECT_MODE_SIMPLE) {
                    values.add(getString(R.string.feature_simple));
                }
            }
        }

        String substring = values.toString().substring(1, values.toString().length() - 1);
        if (key == CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP || key == CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES) {
            return substring.replace(", ", "\n");

        } else {
            return substring;
        }
    }

    private String getRange(Range<Integer> character) {
        return "[" + character.getLower().toString() + "," + character.getUpper().toString() + "]";
    }
}
