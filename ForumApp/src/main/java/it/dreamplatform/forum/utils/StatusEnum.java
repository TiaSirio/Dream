package it.dreamplatform.forum.utils;

/**
 * This enum defines if a Post has been APPROVED (1), is still PENDING (0), or if something went wrong (ERROR -> 2)
 */
public enum StatusEnum {
    PENDING,ACCEPTED,ERROR;

    StatusEnum getStatusById(int statusId) {
        switch (statusId) {
            case 0:
                return StatusEnum.PENDING;
            case 1:
                return StatusEnum.ACCEPTED;
            default:
                return StatusEnum.ERROR;
        }
    }
}