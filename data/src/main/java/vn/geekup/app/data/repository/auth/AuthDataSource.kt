package vn.geekup.app.data.repository.auth

import kotlinx.coroutines.flow.Flow
import vn.geekup.app.data.di.qualifier.source.SourceLocal
import vn.geekup.app.data.di.qualifier.source.SourceRemote
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthDataSource @Inject constructor(

    @SourceLocal
    private val local: AuthRepository,

    @SourceRemote
    private val remote: AuthRepository

) : AuthRepository {

    override fun logout(): Flow<ResultModel<Boolean>> {
        return remote.logout()
    }

    override suspend fun saveToken(token: String): Flow<ResultModel<Unit>> {
        return local.saveToken(token)
    }

    override suspend fun loginOTable(otableBody: OTableRequestBody): Flow<ResultModel<OTableModel>> {
        return remote.loginOTable(otableBody)
    }

    override suspend fun loginWithTravel(): Flow<ResultModel<OTableModel>> {
        return remote.loginWithTravel()
    }
}