package com.dailypit.dp.Interface;

import com.dailypit.dp.Model.AddOnService.AddOnServiceResponse;
import com.dailypit.dp.Model.AddOnService.AddOnServicesCnfResponse;
import com.dailypit.dp.Model.Address.AddAddressResponse;
import com.dailypit.dp.Model.Address.DeleteAddressResponse;
import com.dailypit.dp.Model.Address.UpdateAddressResponse;
import com.dailypit.dp.Model.Address.UserAddressListResponse;
import com.dailypit.dp.Model.Banner.BannerResponse;
import com.dailypit.dp.Model.Banner.BottomBannerResponse;
import com.dailypit.dp.Model.Coupon.CouponResponse;
import com.dailypit.dp.Model.Coupon.HiddenCouponResponse;
import com.dailypit.dp.Model.Favourite.MYFavouriteServiceResponse;
import com.dailypit.dp.Model.FavouriteServiceResponse;
import com.dailypit.dp.Model.HelpRequestResponse;
import com.dailypit.dp.Model.HowItWorkResponse;
import com.dailypit.dp.Model.HowItWorks.HowitWorksResponse;
import com.dailypit.dp.Model.MakeAddressDefaultResponse;
import com.dailypit.dp.Model.MemberShip.AddMemberResponse;
import com.dailypit.dp.Model.MemberShip.CheckMemberShipPlan.CheckMemberShipPlan;
import com.dailypit.dp.Model.MemberShip.MemberShipContentList;
import com.dailypit.dp.Model.MemberShip.MemberShipPlanListResponse;
import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsResponse;
import com.dailypit.dp.Model.Notification.NotificationResponse;
import com.dailypit.dp.Model.OTPResponse;
import com.dailypit.dp.Model.OrderStatus.CompleteOrderResponse;
import com.dailypit.dp.Model.OrderStatus.OngoingOrderResponse;
import com.dailypit.dp.Model.Packageg.CategoryResponse;
import com.dailypit.dp.Model.Packageg.CheckPackageResponse;
import com.dailypit.dp.Model.Packageg.PackagePlanlistResponse;
import com.dailypit.dp.Model.Packageg.PackageResponse;
import com.dailypit.dp.Model.Packageg.PurchasePackageResponse;
import com.dailypit.dp.Model.PayOnlineResponse;
import com.dailypit.dp.Model.Payment.PaymentResponse;
import com.dailypit.dp.Model.Policy.AppDetailsResponse;
import com.dailypit.dp.Model.Profile.UpdateUserImageProfileResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponse;
import com.dailypit.dp.Model.Profile.UserUpdateProfile;
import com.dailypit.dp.Model.ResendOTPResponse;
import com.dailypit.dp.Model.Service.ServiceChildCategoryResponse;
import com.dailypit.dp.Model.Service.ServiceResponse;
import com.dailypit.dp.Model.Service.ServiceSubCategoryResponse;
import com.dailypit.dp.Model.SignUpResponse;
import com.dailypit.dp.Model.State.StateResponse;
import com.dailypit.dp.Model.Wallet.AddAmountResponse;
import com.dailypit.dp.Model.Wallet.WalletMoneyResponse;
import com.dailypit.dp.Model.Wallet.WalletTransactionResponse;
import com.dailypit.dp.Model.loginResponse;
import com.dailypit.dp.Model.logoutResponse;
import com.dailypit.dp.Utils.Constants;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceInterface {

    @POST(Constants.SIGNUP)
    Call<SignUpResponse> doSignup(@Body HashMap<String, String> map);

    @POST(Constants.LOGIN)
    Call<loginResponse> doLogin(@Body HashMap<String, String> map);

    @POST(Constants.OTP)
    Call<OTPResponse> getOTP(@Body HashMap<String, String> map);

    @POST(Constants.RESENDOTP)
    Call<ResendOTPResponse> getResendOTP(@Body HashMap<String, String> map);

    @GET(Constants.BANNER)
    Call<BannerResponse> getBanner();

    @GET(Constants.BOTTOMBANNER)
    Call<BottomBannerResponse> getBottomBanner();

    @POST(Constants.ADDNEWADDRESS)
    Call<AddAddressResponse> addAddress(@Body HashMap<String, String> map);

    @POST(Constants.UPDATEADDRESS)
    Call<UpdateAddressResponse> updateAddress(@Body HashMap<String, String> map);

    @POST(Constants.USERADDRESSLIST)
    Call<UserAddressListResponse> getUserAddress(@Body HashMap<String, String> map);

    @POST(Constants.DELETEUSERADDRESS)
    Call<DeleteAddressResponse> deleteUserAddress(@Body HashMap<String, String> map);

    @GET(Constants.STATE)
    Call<StateResponse> getState();

    @POST(Constants.USERPROFILE)
    Call<UserProfileResponse> getUserProfile(@Body HashMap<String, String> map);

    @POST(Constants.UPDATEPROFILE)
    Call<UserUpdateProfile> updateUserProfile(@Body HashMap<String, String> map);

    @POST(Constants.UPDATEPIMAGEROFILE)
    Call<UpdateUserImageProfileResponse> updateUserImageProfile(@Body HashMap<String, String> map);

    @POST(Constants.NOTIFICATION)
    Call<NotificationResponse> getNotification(@Body HashMap<String, String> map);

    @POST(Constants.COUPONS)
    Call<CouponResponse> getCoupons(@Body HashMap<String, String> map);

    @POST(Constants.HIDDENCOUPON)
    Call<HiddenCouponResponse> getHiddenCoupons(@Body HashMap<String, String> map);

    @GET(Constants.SERVICES)
    Call<ServiceResponse> getServices();

    @POST(Constants.SUBCATEGORY)
    Call<ServiceSubCategoryResponse> getSubCategory(@Body HashMap<String, String> map);

    @POST(Constants.CHILDCATEGORY)
    Call<ServiceChildCategoryResponse> getChildCategory(@Body HashMap<String, String> map);

    @POST(Constants.PLACEORDER)
    Call<PaymentResponse> placeOrder(@Body HashMap<String, String> map);

    @POST(Constants.ORDERSTATUS)
    Call<OngoingOrderResponse> getOngoingOrder(@Body HashMap<String, String> map);

    @POST(Constants.ORDERSTATUS)
    Call<CompleteOrderResponse> getCompleteOrder(@Body HashMap<String, String> map);

    @POST(Constants.HELP)
    Call<HelpRequestResponse> helpRequest(@Body HashMap<String, String> map);

    @POST(Constants.HELPORDER)
    Call<HelpRequestResponse> helpOrderRequest(@Body HashMap<String, String> map);

    @GET(Constants.APPDETAILS)
    Call<AppDetailsResponse> getAppDetails();

    @POST(Constants.ADDRESSDEFAULT)
    Call<MakeAddressDefaultResponse> makeDefaultAddress(@Body HashMap<String, String> map);

    @POST(Constants.PAYONLINE)
    Call<PayOnlineResponse> payOnline(@Body HashMap<String, String> map);

    @POST(Constants.WALLETMONEY)
    Call<WalletMoneyResponse> getWalletMoney(@Body HashMap<String, String> map);

    @POST(Constants.WALLETTRANSACTION)
    Call<WalletTransactionResponse> getWalletTransactionList(@Body HashMap<String, String> map);

    @POST(Constants.ADDAMOUNT)
    Call<AddAmountResponse> getAmountinWallet(@Body HashMap<String, String> map);

    @POST(Constants.MAKEFAVOURITE)
    Call<FavouriteServiceResponse> getFavourite(@Body HashMap<String, String> map);

    @POST(Constants.MYFAVOURITE)
    Call<MYFavouriteServiceResponse> getFavouriteList(@Body HashMap<String, String> map);

    @POST(Constants.LOGOUT)
    Call<logoutResponse> getLogout(@Body HashMap<String, String> map);

    @POST(Constants.ADDONSERVICES)
    Call<AddOnServiceResponse> addNewServices(@Body HashMap<String, String> map);

    @POST(Constants.ADDONSERVICESCNF)
    Call<AddOnServicesCnfResponse> addNewServicesCnf(@Body HashMap<String, String> map);

    @POST(Constants.ADDONSERVICESCNFED)
    Call<AddOnServiceResponse> addNewServicesCnfed(@Body HashMap<String, String> map);

    @GET(Constants.HOWITWORKS)
    Call<HowitWorksResponse> getAppContant();

    @GET(Constants.ADDMEMBERSHIPPLAN)
    Call<MemberShipPlanListResponse> getMemberShipPlanList();

    @POST(Constants.ADDMEMBERSHIP)
    Call<AddMemberResponse> addMemberShip(@Body HashMap<String, String> map);

    @POST(Constants.ISMEMBERSHIPPLAN)
    Call<CheckMemberShipPlan> idMemberShipPlan(@Body HashMap<String, String> map);

    @GET(Constants.PLANDETAILS)
    Call<PlanDetailsResponse> getPlanDiscount();

    @GET(Constants.CONTENTLIST)
    Call<MemberShipContentList> getContentList();

    @POST(Constants.PACKAGE)
    Call<PackageResponse> getPackageg(@Body HashMap<String, String> map);

    @POST(Constants.PACKAGEDETAILS)
    Call<PackagePlanlistResponse> getPackageDetails(@Body HashMap<String, String> map);

    @POST(Constants.PACKAGEPLAN)
    Call<PurchasePackageResponse> getPackagePlan(@Body HashMap<String, String> map);

    @POST(Constants.MYPACKAGEPLAN)
    Call<CheckPackageResponse> getMyPackage(@Body HashMap<String, String> map);

    @POST(Constants.PACKAGECATEGORYID)
    Call<CategoryResponse> getpackageCategoryList(@Body HashMap<String, String> map);


}
