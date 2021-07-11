package vn.gas.thq.ui.retailtongdaily

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService
import vn.gas.thq.ui.retail.GasRemainModel
import vn.gas.thq.ui.retail.RequestInitRetail

class RetailBossRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun onGetListCustomer(lat: String?, lng: String?) = flow {
        emit(apiService.getListCustomerBoss(lat, lng))
    }

    suspend fun getGiaKHBHTongDaiLy(cust_id: Int, product_code: String) =
        flow { emit(apiService.getGiaTongDaiLy(product_code, cust_id)) }

    suspend fun getGiaNiemYet(customer_id: String, product_code: String, sale_type: String) =
        flow { emit(apiService.getGiaNiemYet(customer_id, product_code, sale_type)) }

    suspend fun getPhiVanChuyen(obj: RequestInitRetailBoss) =
        flow { emit(apiService.getPhiVanChuyen(obj)) }

    suspend fun doRequestRetailBoss(obj: RequestInitRetailBoss) = flow {
        emit(apiService.doRequestRetailBoss(obj))
    }

    suspend fun doRetailBossLXBH(orderId: String?, obj: GasRemainModel) = flow {
        emit(apiService.doRetailTDLLXBH(orderId, obj))
    }
}