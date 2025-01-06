package vip.gpg123.common.exception.file;

import vip.gpg123.common.exception.base.BaseException;

/**
 * 文件信息异常类
 *
 * @author gpg123
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
