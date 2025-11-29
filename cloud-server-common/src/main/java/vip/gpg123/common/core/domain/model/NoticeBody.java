package vip.gpg123.common.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vip.gpg123.common.core.domain.BaseBody;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeBody extends BaseBody implements Serializable {

}
