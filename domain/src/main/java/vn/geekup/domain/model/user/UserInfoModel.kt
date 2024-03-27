package vn.geekup.domain.model.user

import vn.geekup.domain.model.general.BaseModel

data class UserInfoModel(
    val id: Int = 0,
    val email: String = "",
    val shortName: String = "",
    val currentLevelId: Int = 0,
    val isFormFill: Boolean = false,
    val firstDay: String = "",
    val avatar: String = "",
    val avatarMediumSize: String = "",
    val avatarBigSize: String = "",
    val avatarSmallSize: String = "",
    val permanentAddress: String = "",
    val isWelcomeKitConfirm: Boolean = false,
    val aliaPoint: Int = 0,
    val isCheckAdventureMap: Boolean = false,
    val adventureIcon: String = ""
) : BaseModel()