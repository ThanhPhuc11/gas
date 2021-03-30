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

    suspend fun getGiaNiemYet(customer_id: String, product_code: String, sale_type: String) =
        flow { emit(apiService.getGiaNiemYet(customer_id, product_code, sale_type)) }

    suspend fun doRequestRetail(obj: RequestInitRetail) = flow {
        emit(apiService.doRequestRetail(obj))
    }

    suspend fun doRetailLXBH(orderId: String?, obj: GasRemainModel) = flow {
        emit(apiService.doRetailLXBH(orderId, obj))
    }
}